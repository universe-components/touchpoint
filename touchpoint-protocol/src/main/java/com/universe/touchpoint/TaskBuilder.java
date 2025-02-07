package com.universe.touchpoint;

import com.universe.touchpoint.config.Model;

public class TaskBuilder {

    private final AgentConfig config = new AgentConfig();
    private static TaskBuilder builder;

    public static TaskBuilder task(String task) {
        builder = new TaskBuilder();
        builder.config.setTask(task);
        return builder;
    }

    public static TaskBuilder model(Model model) {
        builder = new TaskBuilder();
        builder.config.getModelConfig().setModel(model);
        return builder;
    }

    public TaskBuilder setModel(Model model) {
        config.getModelConfig().setModel(model);
        return this;
    }

    public TaskBuilder setTemperature(Float temperature) {
        config.getModelConfig().setTemperature(temperature);
        return this;
    }

    public TaskBuilder setModelApiKey(String apiKey) {
        config.getModelConfig().setApiKey(apiKey);
        return this;
    }

    public TaskBuilder build() {
        return this;
    }

    public String run(String content) {
        return Dispatcher.dispatch(content, config.getTask());
    }

    public static TaskBuilder getBuilder() {
        return builder;
    }

    public AgentConfig getConfig() {
        return config;
    }

}
