package com.universe.touchpoint;

import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.MQTTConfig;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;

import java.util.HashMap;
import java.util.Map;

public class AgentConfig {

    private volatile LangModelConfig modelConfig = new LangModelConfig();
    private VisionModelConfig visionModelConfig = new VisionModelConfig();
    private VisionLangModelConfig visionLangModelConfig = new VisionLangModelConfig();
    private volatile TransportConfig<?> transportConfig;
    private AgentSocketConfig socketConfig = new AgentSocketConfig();
    private MetricSocketConfig metricSocketConfig = new MetricSocketConfig();
    private ActionMetricConfig actionMetricConfig = new ActionMetricConfig();
    private TaskMetricConfig taskMetricConfig = new TaskMetricConfig();

    private final Map<Transport, Class<?>> transportConfigMap = new HashMap<>();
    {
        transportConfigMap.put(Transport.DUBBO, DubboConfig.class);
        transportConfigMap.put(Transport.MQTT, MQTTConfig.class);
    }

    public LangModelConfig getModelConfig() {
        return modelConfig;
    }

    public void setModelConfig(LangModelConfig modelConfig) {
        this.modelConfig = modelConfig;
    }

    public VisionModelConfig getVisionModelConfig() {
        return visionModelConfig;
    }

    public void setVisionModelConfig(VisionModelConfig visionModelConfig) {
        this.visionModelConfig = visionModelConfig;
    }

    public VisionLangModelConfig getVisionLangModelConfig() {
        return visionLangModelConfig;
    }

    public void setVisionLangModelConfig(VisionLangModelConfig visionLangModelConfig) {
        this.visionLangModelConfig = visionLangModelConfig;
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

    public MetricSocketConfig getMetricSocketConfig() {
        return metricSocketConfig;
    }

    public void setMetricSocketConfig(MetricSocketConfig metricSocketConfig) {
        this.metricSocketConfig = metricSocketConfig;
    }

    public ActionMetricConfig getActionMetricConfig() {
        return actionMetricConfig;
    }

    public void setActionMetricConfig(ActionMetricConfig actionMetricConfig) {
        this.actionMetricConfig = actionMetricConfig;
    }

    public TaskMetricConfig getTaskMetricConfig() {
        return taskMetricConfig;
    }

    public void setTaskMetricConfig(TaskMetricConfig taskMetricConfig) {
        this.taskMetricConfig = taskMetricConfig;
    }

}