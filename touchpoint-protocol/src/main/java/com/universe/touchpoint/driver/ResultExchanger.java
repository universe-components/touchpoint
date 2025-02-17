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
            ((AgentAction<?, ?>) result).getMetric().getPredictionCount().incrementAndGet();
            TaskMetricManager.addTaskMetric((AgentAction<?, ?>) result);
            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor<I, O>();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        return resultProcessor.process(result, goal, task, context, transportType);
    }

}
