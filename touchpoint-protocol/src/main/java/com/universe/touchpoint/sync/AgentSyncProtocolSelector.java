package com.universe.touchpoint.sync;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.sync.protocol.MQTT5Protocol;

public class AgentSyncProtocolSelector {

    public static AgentSyncProtocol selectProtocol(SocketProtocol protocol) {
        return switch (protocol) {
            case MQTT5 -> new MQTT5Protocol();
        };
    }

}
