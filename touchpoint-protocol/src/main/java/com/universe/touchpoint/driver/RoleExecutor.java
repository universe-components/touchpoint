package com.universe.touchpoint.driver;

import java.util.HashMap;
import java.util.Map;

public class RoleExecutor<Executor> {

    private final Map<String, Executor> executorMap = new HashMap<>();

    public void registerExecutor(String action, Executor executor) {
        executorMap.put(action, executor);
    }

    public Executor getExecutor(String action) {
        return executorMap.get(action);
    }

}
