package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.api.RoleConstants;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.monitor.action.alarm.TaskMonitor;
import com.universe.touchpoint.monitor.action.alarm.ActionMonitor;
import com.universe.touchpoint.monitor.action.fetcher.TaskMetricFetcher;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoleExecutorContainer {

    private final Map<String, RoleExecutor<?, ?>> executorMap = new ConcurrentHashMap<>();
    {
        executorMap.put(RoleConstants.ACTION_CAPABILITY_CHECKER, new ActionMonitor<>());
        executorMap.put(RoleConstants.TASK_EXECUTOR_CHECKER, new TaskMonitor<>());
        executorMap.put(RoleConstants.TASK_METRIC_FETCHER, new TaskMetricFetcher<>());
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
