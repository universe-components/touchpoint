package com.universe.touchpoint.driver;

import java.util.HashMap;
import java.util.Map;

public class CollaborationFactory {

    private static final Object lock = new Object();

    private static final Map<String, Collaboration> collaborationMap = new HashMap<>();

    // 获取单例实例
    public static Collaboration getInstance(String task) {
        if (!collaborationMap.containsKey(task)) {
            synchronized (lock) {
                if (!collaborationMap.containsKey(task)) {
                    collaborationMap.put(task, new Collaboration());
                }
            }
        }
        return collaborationMap.get(task);
    }

}
