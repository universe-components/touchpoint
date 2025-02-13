package com.universe.touchpoint;

import com.universe.touchpoint.config.Model;

import java.util.HashMap;
import java.util.Map;

public class TaskBuilder {

    private static final Object lock = new Object();

    private final String task;
    private final AgentConfig config = new AgentConfig();
    private static final Map<String, TaskBuilder> builderMap = new HashMap<>();

    public static TaskBuilder task(String task) {
        if (!builderMap.containsKey(task)) {
            synchronized (lock) {
                if (!builderMap.containsKey(task)) {
                    builderMap.put(task, new TaskBuilder(task));
                }
            }
        }
        return builderMap.get(task);
    }

    public TaskBuilder(String task) {
        this.task = task;
    }

    public TaskBuilder model(Model model) {
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
        return Dispatcher.dispatch(content, task);
    }

    public static TaskBuilder getBuilder(String task) {
        return builderMap.get(task);
    }

    public AgentConfig getConfig() {
        return config;
    }

}
