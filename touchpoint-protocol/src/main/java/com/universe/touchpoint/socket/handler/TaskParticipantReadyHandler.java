package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.driver.ActionGraph;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<TransportConfig<?>> {

    @Override
    public TransportConfig<?> onStateChange(Object actionConfig, Context context) {
        if (actionConfig != null) {
            ActionGraph.getInstance().addActionConfig((ActionConfig) actionConfig);
        }
        return AgentBuilder.getBuilder().getConfig().getTransportConfig();
    }

}
