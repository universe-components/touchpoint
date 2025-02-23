package com.universe.touchpoint.plan;

import java.util.HashMap;
import java.util.Map;

public class ActionGraphBuilder {

    private static final Object lock = new Object();
    private static final Map<String, ActionGraph> actionGraphMap = new HashMap<>();

    // 获取单例实例
    public static ActionGraph getTaskGraph(String task) {
        if (!actionGraphMap.containsKey(task)) {
            synchronized (lock) {
                if (!actionGraphMap.containsKey(task)) {
                    actionGraphMap.put(task, new ActionGraph());
                }
            }
        }
        return actionGraphMap.get(task);
    }

    public static void putGraph(String task, ActionGraph actionGraph) {
        actionGraphMap.put(task, actionGraph);
    }

}
