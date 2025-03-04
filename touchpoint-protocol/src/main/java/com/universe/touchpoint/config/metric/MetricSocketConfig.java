package com.universe.touchpoint.config.metric;

import com.universe.touchpoint.annotations.socket.SocketProtocol;

public class MetricSocketConfig {

    private SocketProtocol bindProtocol;
    String brokerUri = "tcp://localhost:1883";

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
