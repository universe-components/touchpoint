package com.universe.touchpoint;

import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;

import java.util.HashMap;
import java.util.Map;

public class AgentConfig {

    private final AIModelConfig modelConfig = new AIModelConfig();
    private TransportConfig<?> transportConfig;
    private Transport transportType;

    private final Map<Transport, Class<?>> transportConfigMap = new HashMap<>();
    {
        transportConfigMap.put(Transport.DUBBO, DubboConfig.class);
    }

    public AIModelConfig getModelConfig() {
        return modelConfig;
    }

    public TransportConfig<?> getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(TransportConfig<?> transportConfig) {
        this.transportConfig = transportConfig;
    }

    public Transport getTransportType() {
        return transportType;
    }

    public void setTransportType(Transport transportType) {
        this.transportType = transportType;
    }

    public Map<Transport, Class<?>> getTransportConfigMap() {
        return transportConfigMap;
    }

}
