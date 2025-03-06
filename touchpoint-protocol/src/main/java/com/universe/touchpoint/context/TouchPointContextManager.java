package com.universe.touchpoint.context;

import java.util.HashMap;
import java.util.Map;

public class TouchPointContextManager {

    private static final Object lock = new Object();
    private final static Map<String, TouchPointContext> touchPointContextMap = new HashMap<>();

    public static TouchPointContext getTouchPointContext(String task) {
        if (!touchPointContextMap.containsKey(task)) {
            synchronized (lock) {
                if (!touchPointContextMap.containsKey(task)) {
                    touchPointContextMap.put(task, new TouchPointContext(task));
                }
            }
        }
        return touchPointContextMap.get(task);
    }

    public static void putTouchPointContext(String task, TouchPointContext touchPointContext) {
        touchPointContextMap.put(task, touchPointContext);
    }

}
