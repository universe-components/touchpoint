package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.processor.AgentFinishProcessor;
import com.universe.touchpoint.monitor.TaskMetricManager;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.driver.processor.AgentActionProcessor;
import com.universe.touchpoint.driver.processor.DefaultResultProcessor;

import java.util.Map;

public class ResultExchanger {

    public static <I extends TouchPoint, O extends TouchPoint, R extends TouchPoint> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        if (result instanceof AgentAction<?, ?>) {
            int stateCode = ((AgentAction<I, O>) result).getInput().getState().getCode();
            if (stateCode >= 300 && stateCode < 400) {
                CoordinatorFactory.getCoordinator(task).execute((AgentAction<I, O>) result, task, context);
            } else if (stateCode >= 400) {
                SupervisorFactory.getSupervisor(task).execute((AgentAction<I, O>) result, task);
            }
        }

        ResultProcessor<R> resultProcessor;
        if (result instanceof AgentAction<?, ?>) {
            String countAction = ((AgentAction<?, ?>) result).getAction();
            TaskMetricManager.getActionMetric(task, countAction).incrementPredictionCount();

            int actionRetryCount = TaskMetricManager.getActionMetric(task, ((AgentAction<?, ?>) result).getAction()).getPredictionCount();
            if (actionRetryCount > 0) {
                TaskMetricManager.getTaskMetric(task).setRetryActionCount(actionRetryCount);
            }

            Map<String, Integer> metrics = Map.of(
                    task + "||" + countAction, TaskMetricManager.getActionMetric(task, countAction).getPredictionCount(),
                    task, TaskMetricManager.getTaskMetric(task).getRetryActionCount());
            TaskMetricManager.getListener(task).send(metrics, context, task);

            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor<I, O>();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        return resultProcessor.process(result, goal, task, context, transportType);
    }

}
