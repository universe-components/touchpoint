package com.universe.touchpoint;

public class AgentBuilder {

    private final AgentConfig config = new AgentConfig();
    private static final AgentBuilder builder = new AgentBuilder();

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
