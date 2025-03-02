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

    public <R extends TouchPoint> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        ResultProcessor<R> resultProcessor;
        if (result instanceof AgentAction<?, ?>) {
            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        Pair<List<AgentAction<?, ?>>, AgentFinish<?>> processedResult = resultProcessor.process(result, goal, task, context, transportType);
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
