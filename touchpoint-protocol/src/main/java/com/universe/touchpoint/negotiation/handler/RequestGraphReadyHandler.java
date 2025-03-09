package com.universe.touchpoint.negotiation.handler;

import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;

public class RequestGraphReadyHandler implements AgentSocketStateHandler<Boolean, ActionGraph> {

    @Override
    public <C extends AgentContext> ActionGraph onStateChange(Boolean ready, C agentContext, String task) {
        if (ready) {
            return ActionGraphBuilder.getTaskGraph(task);
        }
        return null;
    }

}
