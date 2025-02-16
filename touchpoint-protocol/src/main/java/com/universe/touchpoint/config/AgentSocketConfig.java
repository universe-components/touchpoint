package com.universe.touchpoint.config;

import com.universe.touchpoint.annotations.socket.SocketProtocol;

public class AgentSocketConfig {

    private SocketProtocol bindProtocol;
    String brokerUri;

    public SocketProtocol getBindProtocol() {
        return bindProtocol;
    }

    public void setBindProtocol(SocketProtocol bindProtocol) {
        this.bindProtocol = bindProtocol;
    }

    public String getBrokerUri() {
        return brokerUri;
    }

    public void setBrokerUri(String brokerUri) {
        this.brokerUri = brokerUri;
    }

}
