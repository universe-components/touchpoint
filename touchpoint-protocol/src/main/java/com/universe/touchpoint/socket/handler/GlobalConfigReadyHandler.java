package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class GlobalConfigReadyHandler implements AgentSocketStateHandler<ActionGraph> {

    @Override
    public <C extends AgentContext> ActionGraph onStateChange(Object ready, C agentContext, Context context, String task) {
        if ((Boolean) ready) {
            return ActionGraphBuilder.getTaskGraph(task);
        }
        return null;
    }

}
