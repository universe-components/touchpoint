package com.universe.touchpoint.rolemodel;

import java.util.HashMap;
import java.util.Map;

public class TaskExecutorFactory {

    private static final Object lock = new Object();

    private static final Map<String, RoleExecutorManager> roleExecutorMap = new HashMap<>();

    // 获取单例实例
    public static RoleExecutorManager getInstance(String task) {
        if (!roleExecutorMap.containsKey(task)) {
            synchronized (lock) {
                if (!roleExecutorMap.containsKey(task)) {
                    roleExecutorMap.put(task, new RoleExecutorManager());
                }
            }
        }
        return roleExecutorMap.get(task);
    }

}
