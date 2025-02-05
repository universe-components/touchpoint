package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class TransportConfigDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @Override
    public Boolean onStateChange(Object transportConfig, Context context) {
        TransportConfig<?> config = (TransportConfig<?>) transportConfig;
        if (config != null && config.config() instanceof RPCConfig) {
            AgentBuilder.getBuilder().getConfig().setTransportConfig(config);
        }
        return true;
    }

}
