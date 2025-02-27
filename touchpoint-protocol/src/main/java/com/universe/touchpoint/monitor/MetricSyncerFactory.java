package com.universe.touchpoint.monitor;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.monitor.syncer.AndroidBroadcastSyncer;
import com.universe.touchpoint.monitor.syncer.MQTT5Syncer;
import com.universe.touchpoint.plan.ActionGraph;

import java.util.HashMap;
import java.util.Map;

public class MetricSyncerFactory {

    private static final Object lock = new Object();
    private static final Map<String, MetricSyncer> metricSyncerMap = new HashMap<>();

    public static MetricSyncer getSyncer(String task) {
        return metricSyncerMap.get(task);
    }

    public static MetricSyncer getSyncer(String task, SocketProtocol mode) {
        if (!metricSyncerMap.containsKey(task)) {
            synchronized (lock) {
                if (!metricSyncerMap.containsKey(task)) {
                    if (mode == SocketProtocol.ANDROID_BROADCAST) {
                        metricSyncerMap.put(task, new AndroidBroadcastSyncer(task));
                    } else {
                        metricSyncerMap.put(task, new MQTT5Syncer(task));
                    }
                }
            }
        }
        return metricSyncerMap.get(task);
    }

}
