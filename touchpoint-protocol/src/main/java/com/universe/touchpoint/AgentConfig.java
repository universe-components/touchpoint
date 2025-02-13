package com.universe.touchpoint;

import com.universe.touchpoint.annotations.SocketProtocol;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;

import java.util.HashMap;
import java.util.Map;

public class AgentConfig {

    private AIModelConfig modelConfig = new AIModelConfig();
    private TransportConfig<?> transportConfig;
    private SocketProtocol socketProtocol;

    private final Map<Transport, Class<?>> transportConfigMap = new HashMap<>();
    {
        transportConfigMap.put(Transport.DUBBO, DubboConfig.class);
    }

    public AIModelConfig getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(AIModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public TransportConfig<?> getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(TransportConfig<?> transportConfig) {
        this.transportConfig = transportConfig;
    }

    public Map<Transport, Class<?>> getTransportConfigMap() {
        return transportConfigMap;
    }

    public SocketProtocol getSocketProtocol() {
        return socketProtocol;
    }

    public void setSocketProtocol(SocketProtocol socketProtocol) {
        this.socketProtocol = socketProtocol;
    }

}
