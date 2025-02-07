package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(Object actionConfig, C agentContext, Context context, String task) {
        if (actionConfig != null) {
            for (String action : ((ActionConfig) actionConfig).getToActions()) {
                ActionGraphBuilder.getTaskGraph(task).addEdge((ActionConfig) actionConfig, new ActionConfig(action));
                ActionGraphBuilder.getTaskGraph(task).updateNodeDesc((ActionConfig) actionConfig);
            }
        }
        return Pair.create(
            AgentBuilder.getBuilder().getConfig().getTransportConfig(),
            AgentBuilder.getBuilder().getConfig().getModelConfig()
        );
    }

}
