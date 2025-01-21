package com.universe.touchpoint;

public class AgentBuilder {

    private final AgentConfig config = new AgentConfig();
    private static AgentBuilder builder;

    public static AgentBuilder createConfig(AgentConfig.Model model) {
        builder = new AgentBuilder();
        builder.config.setModel(model);

        return builder;
    }

    public AgentBuilder setModelApiKey(String apiKey) {
        config.setModelApiKey(apiKey);
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
