package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import java.util.List;
import java.util.Map;

public class GlobalConfigReadyHandler implements AgentSocketStateHandler<Map<String, List<String>>> {

    @Override
    public <C extends AgentContext> Map<String, List<String>> onStateChange(Object ready, C agentContext, Context context, String task) {
        if ((Boolean) ready) {
            return ActionGraph.getInstance().getSubGraph(task);
        }
        return null;
    }

}
