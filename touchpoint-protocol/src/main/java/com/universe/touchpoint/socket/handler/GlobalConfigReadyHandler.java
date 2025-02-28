package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class GlobalConfigReadyHandler implements AgentSocketStateHandler<Boolean, ActionGraph> {

    @Override
    public <C extends AgentContext> ActionGraph onStateChange(Boolean ready, C agentContext, Context context, String task) {
        if (ready) {
            return ActionGraphBuilder.getTaskGraph(task);
        }
        return null;
    }

}
