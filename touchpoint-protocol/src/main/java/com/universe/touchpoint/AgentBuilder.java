package com.universe.touchpoint;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;

public class AgentBuilder {

    private final AgentConfig config = new AgentConfig();
    private static AgentBuilder builder;

    public static AgentBuilder task(String task) {
        builder = new AgentBuilder();
        builder.config.setTask(task);
        return builder;
    }

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

    public AgentBuilder build() {
        if (config.getModelConfig().getModel() != null) {
            AgentSocketStateMachine.getInstance().send(
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.AI_MODEL_DISTRIBUTED, config.getModelConfig()),
                    Agent.getContext(),
                    config.getTask());
        }
        return this;
    }

    public String run(String content) {
        return Dispatcher.dispatch(content, config.getTask());
    }

    public static AgentBuilder getBuilder() {
        return builder;
    }

    public AgentConfig getConfig() {
        return config;
    }

}
