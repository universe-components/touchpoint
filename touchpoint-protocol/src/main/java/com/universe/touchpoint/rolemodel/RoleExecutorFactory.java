package com.universe.touchpoint.rolemodel;

import java.util.HashMap;
import java.util.Map;

public class RoleExecutorFactory {

    private static final Object lock = new Object();

    private static final Map<String, RoleExecutor<?>> roleExecutorMap = new HashMap<>();

    // 获取单例实例
    public static <E> RoleExecutor<E> getInstance(String task) {
        if (!roleExecutorMap.containsKey(task)) {
            synchronized (lock) {
                if (!roleExecutorMap.containsKey(task)) {
                    roleExecutorMap.put(task, new RoleExecutor<>());
                }
            }
        }
        return (RoleExecutor<E>) roleExecutorMap.get(task);
    }

}
