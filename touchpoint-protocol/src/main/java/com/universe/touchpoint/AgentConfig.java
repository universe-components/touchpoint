package com.universe.touchpoint;

import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.task.ActionMetricConfig;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;

import java.util.HashMap;
import java.util.Map;

public class AgentConfig {

    private AIModelConfig modelConfig = new AIModelConfig();
    private TransportConfig<?> transportConfig;
    private AgentSocketConfig socketConfig = new AgentSocketConfig();
    private ActionMetricConfig actionMetricConfig = new ActionMetricConfig();

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

    public AgentSocketConfig getSocketConfig() {
        return socketConfig;
    }

    public void setSocketConfig(AgentSocketConfig socketConfig) {
        this.socketConfig = socketConfig;
    }

    public ActionMetricConfig getActionMetricConfig() {
        return actionMetricConfig;
    }

    public void setActionMetricConfig(ActionMetricConfig actionMetricConfig) {
        this.actionMetricConfig = actionMetricConfig;
    }

}