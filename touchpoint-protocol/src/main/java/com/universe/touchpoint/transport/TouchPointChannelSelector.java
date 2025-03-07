package com.universe.touchpoint.transport;

import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.context.TouchPointContext;

import java.util.List;

public class TouchPointChannelSelector extends AgentActionExecutor<String, TouchPointChannel<?>> {

    public TouchPointChannelSelector() {
        this.internalParams = List.of(Transport.DUBBO.name(), Transport.MQTT.name());
    }

    @Override
    public TouchPointChannel<?> run(String goal, TouchPointContext context) {
        return null;
    }

}
