package com.universe.touchpoint.rolemodel;

import java.util.HashMap;
import java.util.Map;

public class TaskRoleExecutor {

    private static final Object lock = new Object();

    private static final Map<String, RoleExecutorContainer> roleExecutorMap = new HashMap<>();

    // 获取单例实例
    public static RoleExecutorContainer getInstance(String task) {
        if (!roleExecutorMap.containsKey(task)) {
            synchronized (lock) {
                if (!roleExecutorMap.containsKey(task)) {
                    roleExecutorMap.put(task, new RoleExecutorContainer());
                }
            }
        }
        return roleExecutorMap.get(task);
    }

}
