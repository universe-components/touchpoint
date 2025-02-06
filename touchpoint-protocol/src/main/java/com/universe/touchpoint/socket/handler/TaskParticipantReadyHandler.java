package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.driver.ActionGraph;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(Object actionConfig, C agentContext, Context context, String task) {
        if (actionConfig != null) {
            ActionGraph.getInstance().addActionConfig((ActionConfig) actionConfig, task);
        }
        return Pair.create(
            AgentBuilder.getBuilder().getConfig().getTransportConfig(),
            AgentBuilder.getBuilder().getConfig().getModelConfig()
        );
    }

}
