package com.universe.touchpoint.guide;

import java.util.HashMap;
import java.util.Map;

public class TaskRoleExecutor {

  private static final Object lock = new Object();

  private static final Map<String, RoleExecutorContainer> roleExecutorMap = new HashMap<>();

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
