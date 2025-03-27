package com.universe.touchpoint.memory;

import java.util.HashMap;
import java.util.Map;

public class ActivityGraphBuilder {

  private static final Object lock = new Object();
  private static final Map<String, ActivityGraph> activityGraphMap = new HashMap<>();

  // 获取单例实例
  public static ActivityGraph getActivityGraph(String task) {
    if (!activityGraphMap.containsKey(task)) {
      synchronized (lock) {
        if (!activityGraphMap.containsKey(task)) {
          activityGraphMap.put(task, new ActivityGraph());
        }
      }
    }
    return activityGraphMap.get(task);
  }

  public static void putGraph(String task, ActivityGraph activityGraph) {
    activityGraphMap.put(task, activityGraph);
  }
}
