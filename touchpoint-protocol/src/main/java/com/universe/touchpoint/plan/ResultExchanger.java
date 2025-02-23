package com.universe.touchpoint.plan;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.processor.AgentFinishProcessor;
import com.universe.touchpoint.plan.processor.AgentActionProcessor;
import com.universe.touchpoint.plan.processor.DefaultResultProcessor;

import java.util.List;

public class ResultExchanger {

    public static <I extends TouchPoint, O extends TouchPoint, R extends TouchPoint> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        ResultProcessor<R> resultProcessor;
        if (result instanceof AgentAction<?, ?>) {
            String countAction = ((AgentAction<?, ?>) result).getActionName();
            result.getContext().getActionContext().getActionMetric(countAction).incrementPredictionCount();

            int actionRetryCount = result.getContext().getActionContext().getActionMetric(countAction).getPredictionCount();
            if (actionRetryCount > 0) {
                result.getContext().getTaskContext().getMetric().setRetryActionCount(actionRetryCount);
            }

            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor<I, O>();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        Pair<List<AgentAction<I, O>>, AgentFinish> processedResult = resultProcessor.process(result, goal, task, context, transportType);
        if (processedResult.first != null) {
            for (AgentAction<?, ?> agentAction : processedResult.first) {
                ResultDispatcher.run(agentAction, agentAction.getMeta(), context);
            }
        } else {
            return ResultDispatcher.run(processedResult.second, processedResult.second.getMeta(), context);
        }

        return null;
    }

}
