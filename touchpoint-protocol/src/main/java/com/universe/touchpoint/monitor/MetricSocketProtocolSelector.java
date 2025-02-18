package com.universe.touchpoint.monitor;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.monitor.listener.AndroidBroadcastMetricListener;
import com.universe.touchpoint.monitor.listener.MQTT5MetricListener;

public class MetricSocketProtocolSelector {

    public static MetricListener selectListener(SocketProtocol protocol) {
        return switch (protocol) {
            case MQTT5 -> new MQTT5MetricListener();
            default -> new AndroidBroadcastMetricListener();
        };
    }

}
