package com.universe.touchpoint;

import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.config.Transport;

public class AgentBuilder {

    private final AgentConfig config = new AgentConfig();
    private static AgentBuilder builder;

    public static AgentBuilder model(Model model) {
        builder = new AgentBuilder();
        builder.config.getModelConfig().setModel(model);
        return builder;
    }

    public AgentBuilder transport(Transport transport) {
        builder = new AgentBuilder();
        builder.config.setTransportType(transport);
        return builder;
    }

    public AgentBuilder setModel(Model model) {
        config.getModelConfig().setModel(model);
        return builder;
    }

    public AgentBuilder setTemperature(Float temperature) {
        config.getModelConfig().setTemperature(temperature);
        return builder;
    }

    public AgentBuilder setModelApiKey(String apiKey) {
        config.getModelConfig().setModelApiKey(apiKey);
        return builder;
    }

    public AgentBuilder setTransportType(Transport transportType) {
        config.setTransportType(transportType);
        return builder;
    }

    public AgentBuilder build() {
        return builder;
    }
    public String run(String content) {
        return Dispatcher.dispatch(content);
    }

    public static AgentBuilder getBuilder() {
        return builder;
    }

    public AgentConfig getConfig() {
        return config;
    }

}
