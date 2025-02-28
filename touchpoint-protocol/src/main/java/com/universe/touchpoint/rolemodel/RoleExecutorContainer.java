package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.api.RoleConstants;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.monitor.action.monitor.TaskMonitor;
import com.universe.touchpoint.monitor.action.monitor.ActionMonitor;

import java.util.HashMap;
import java.util.Map;

public class RoleExecutorContainer {

    private final Map<String, RoleExecutor<?, ?>> executorMap = new HashMap<>();
    {
        executorMap.put(RoleConstants.ACTION_CAPABILITY_CHECKER, new ActionMonitor<>());
        executorMap.put(RoleConstants.TASK_EXECUTOR_CHECKER, new TaskMonitor<>());
    }

    public void registerExecutor(String action, RoleExecutor<?, ?> executor) {
        executorMap.put(action, executor);
    }

    public RoleExecutor<?, ?> getExecutor(String action) {
        return executorMap.get(action);
    }

    public Map<String, RoleExecutor<?, ?>> getExecutorMap() {
        return executorMap;
    }

}
