package com.universe.touchpoint.monitor;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.monitor.metric.ActionGraphMetric;
import com.universe.touchpoint.monitor.metric.ActionMetric;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskMetricManager {

    private static final Object mLock = new Object();

    private static final Map<String, MetricListener> listenerMap = new HashMap<>();
    private static final Map<String, ActionGraphMetric> taskMetrics = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, ActionMetric>> actionMetrics = new ConcurrentHashMap<>();

    public static void registerListener(String task, SocketProtocol protocol) {
        if (!listenerMap.containsKey(task)) {
            synchronized (mLock) {
                if (!listenerMap.containsKey(task)) {
                    listenerMap.put(task, MetricSocketProtocolSelector.selectListener(protocol));
                }
            }
        }
    }

    public static MetricListener getListener(String task) {
        return listenerMap.get(task);
    }

    public static ActionGraphMetric getTaskMetric(String task) {
        return taskMetrics.computeIfAbsent(task, k -> new ActionGraphMetric());
    }

    public static ActionMetric getActionMetric(String task, String action) {
        actionMetrics.computeIfAbsent(task, k -> new ConcurrentHashMap<>());
        Map<String, ActionMetric> actionMap = actionMetrics.get(task);

        assert actionMap != null;
        return actionMap.computeIfAbsent(action, k -> new ActionMetric());
    }

}
