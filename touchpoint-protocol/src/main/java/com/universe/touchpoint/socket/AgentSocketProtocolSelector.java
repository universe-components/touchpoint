package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.transport.SocketProtocol;
import com.universe.touchpoint.socket.protocol.AndroidBroadcast;
import com.universe.touchpoint.socket.protocol.MQTT5;

public class AgentSocketProtocolSelector {

    public static AgentSocketProtocol selectProtocol(SocketProtocol protocol) {
        return switch (protocol) {
            case MQTT5 -> new MQTT5();
            default -> new AndroidBroadcast();
        };
    }

}
