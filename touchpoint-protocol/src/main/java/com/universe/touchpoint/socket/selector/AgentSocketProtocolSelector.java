package com.universe.touchpoint.socket.selector;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.protocol.MQTT5Protocol;

public class AgentSocketProtocolSelector {

    public static AgentSocketProtocol selectProtocol(SocketProtocol protocol) {
        return switch (protocol) {
            case MQTT5 -> new MQTT5Protocol();
        };
    }

}
