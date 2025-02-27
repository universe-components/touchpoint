package com.universe.touchpoint.plan;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.annotations.task.Task;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.processor.AgentFinishProcessor;
import com.universe.touchpoint.plan.processor.AgentActionProcessor;
import com.universe.touchpoint.plan.processor.DefaultResultProcessor;

import java.util.List;

public class ResultExchanger {

    @Task("collect_metrics")
    private TaskBuilder metricTaskBuilder;

    public static <I extends TouchPoint, O extends TouchPoint, R extends TouchPoint> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        ResultProcessor<R> resultProcessor;
        if (result instanceof AgentAction<?, ?>) {
            TaskBuilder.task("collect_metrics").run("I want to collect action and task metrics, where task metrics include the number of execution errors and prediction counts for multiple actions within the task, and action metrics include the prediction count for a single action.");
            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor<I, O>();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        Pair<List<AgentAction<I, O>>, AgentFinish> processedResult = resultProcessor.process(result, goal, task, context, transportType);
        if (processedResult.first != null) {
            for (AgentAction<?, ?> agentAction : processedResult.first) {
                result.getContext().getActionContext().setCurrentAction(agentAction.getActionName());
                ResultDispatcher.run(agentAction, agentAction.getMeta(), context);
            }
        } else {
            return ResultDispatcher.run(processedResult.second, processedResult.second.getMeta(), context);
        }

        return null;
    }

}
