package com.universe.touchpoint;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.transport.TouchPointTransportConfigManager;

public class AgentBuilder {

    private final AgentConfig config = new AgentConfig();
    private static AgentBuilder builder;

    public static AgentBuilder model(Model model) {
        builder = new AgentBuilder();
        builder.config.getModelConfig().setModel(model);
        return builder;
    }

    public AgentBuilder setModel(Model model) {
        config.getModelConfig().setModel(model);
        return this;
    }

    public AgentBuilder setTemperature(Float temperature) {
        config.getModelConfig().setTemperature(temperature);
        return this;
    }

    public AgentBuilder setModelApiKey(String apiKey) {
        config.getModelConfig().setApiKey(apiKey);
        return this;
    }

    public AgentBuilder transport(Transport transport) {
        builder = new AgentBuilder();

        try {
            Class<?> configClass = config.getTransportConfigMap().get(transport);
            assert configClass != null;
            builder.config.setTransportConfig(new TransportConfig<>(
                    transport,
                    configClass.getConstructor().newInstance()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder;
    }

    public AgentBuilder setApplicationName(String applicationName) {
       if (config.getTransportConfig().config() instanceof RPCConfig) {
            ((RPCConfig) config.getTransportConfig().config()).setApplicationName(applicationName);
       }
        return this;
    }

    public AgentBuilder setRegistryAddress(String registryAddress) {
        if (config.getTransportConfig().config() instanceof RPCConfig) {
            ((RPCConfig) config.getTransportConfig().config()).setRegistryAddress(registryAddress);
        }
        return this;
    }

    public AgentBuilder build() {
        if (config.getTransportConfig().config() != null) {
            TouchPointTransportConfigManager.registerTransportConfig(config.getTransportConfig(), Agent.getContext());
        }
        return this;
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
