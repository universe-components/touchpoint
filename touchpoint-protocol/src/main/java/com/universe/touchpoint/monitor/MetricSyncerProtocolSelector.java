package com.universe.touchpoint.monitor;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.monitor.syncer.MQTT5Syncer;

public class MetricSyncerProtocolSelector {

    public static MetricSyncer selectProtocol(SocketProtocol protocol) {
        return switch (protocol) {
            case MQTT5 -> new MQTT5Syncer();
            default -> null;
        };
    }

}
