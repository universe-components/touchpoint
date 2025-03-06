package com.universe.touchpoint.plan;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.processor.AgentFinishProcessor;
import com.universe.touchpoint.plan.processor.AgentActionProcessor;
import com.universe.touchpoint.plan.processor.DefaultResultProcessor;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class ResultExchanger {

    public <R extends TouchPoint> String exchange(
            R result, String goal, String task, Transport transportType) {
        ResultProcessor<R> resultProcessor;
        if (result instanceof AgentAction<?, ?>) {
            resultProcessor = (ResultProcessor<R>) new AgentActionProcessor();
        } else if (result instanceof AgentFinish) {
            resultProcessor = (ResultProcessor<R>) new AgentFinishProcessor();
        } else {
            resultProcessor = new DefaultResultProcessor<>();
        }

        Pair<List<AgentAction<?, ?>>, AgentFinish<?>> processedResult = resultProcessor.process(result, goal, task, transportType);
        if (processedResult.getLeft() != null) {
            for (AgentAction<?, ?> agentAction : processedResult.getLeft()) {
                result.getContext().getActionContext().setCurrentAction(agentAction.getActionName());
                ResultDispatcher.run(agentAction, agentAction.getMeta());
            }
        } else {
            return ResultDispatcher.run(processedResult.getRight(), processedResult.getRight().getMeta());
        }

        return null;
    }

}
