package com.universe.touchpoint.monitor;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import java.util.HashMap;
import java.util.Map;

public class MetricSyncerFactory {

    private static final Object lock = new Object();
    private static final Map<String, MetricSyncer> metricSyncerMap = new HashMap<>();

    public static MetricSyncer getSyncer(String task) {
        return metricSyncerMap.get(task);
    }

    public static MetricSyncer registerSyncer(String task, SocketProtocol mode) {
        if (!metricSyncerMap.containsKey(task)) {
            synchronized (lock) {
                if (!metricSyncerMap.containsKey(task)) {
                    metricSyncerMap.put(task, MetricSyncerProtocolSelector.selectProtocol(mode));
                }
            }
        }
        return metricSyncerMap.get(task);
    }

}
