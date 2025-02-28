package com.universe.touchpoint;

import android.content.Context;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.context.TouchPoint;
import java.util.HashMap;
import java.util.Map;

public class TaskBuilder {

    private static final Object lock = new Object();

    private final String task;
    private final AgentConfig config = new AgentConfig();
    private final TaskCallbackListener callbackListener;
    private static final Map<String, TaskBuilder> builderMap = new HashMap<>();

    public static TaskBuilder task(String task) {
        return task(task, null);
    }

    public static TaskBuilder task(String task, TaskCallbackListener callbackListener) {
        if (!builderMap.containsKey(task)) {
            synchronized (lock) {
                if (!builderMap.containsKey(task)) {
                    builderMap.put(task, new TaskBuilder(task, callbackListener));
                }
            }
        }
        return builderMap.get(task);
    }

    public TaskBuilder(String task, TaskCallbackListener callbackListener) {
        this.task = task;
        this.callbackListener = callbackListener;
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

    public String run(String goal) {
        return Dispatcher.dispatch(goal, task, null);
    }

    public <T extends TouchPoint> String run(String goal, T params) {
        return Dispatcher.dispatch(goal, task, params);
    }

    public static TaskBuilder getBuilder(String task) {
        return builderMap.get(task);
    }

    public AgentConfig getConfig() {
        return config;
    }

    public TaskCallbackListener getCallbackListener() {
        return callbackListener;
    }

    public static abstract class TaskCallbackListener {

        public abstract <T> void onSuccess(T result, Context context);

    }

}
