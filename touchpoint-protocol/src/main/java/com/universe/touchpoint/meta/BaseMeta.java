package com.universe.touchpoint.meta;

import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.transport.TransportConfig;

public abstract class BaseMeta {

    protected String name;
    protected String agentName;
    protected String className;
    protected String desc;
    protected LangModelConfig model;
    protected VisionModelConfig visionModel;
    protected VisionLangModelConfig visionLangModel;
    protected TransportConfig<?> transportConfig;
    protected MetricSocketConfig metricSocketConfig;
    protected AgentSocketConfig agentSocketConfig;
    protected ActionMetricConfig actionMetricConfig;
    protected TaskMetricConfig taskMetricConfig;

    public BaseMeta(String name) {
        this.name = name;
    }

    public BaseMeta(String name,
                    String agentName,
                    String className,
                    String desc,
                    LangModelConfig model,
                    VisionModelConfig visionModel,
                    VisionLangModelConfig visionLangModel,
                    TransportConfig<?> transportConfig,
                    MetricSocketConfig metricSocketConfig,
                    AgentSocketConfig agentSocketConfig,
                    TaskMetricConfig taskMetricConfig,
                    ActionMetricConfig actionMetricConfig) {
        this.name = name;
        this.agentName = agentName;
        this.className = className;
        this.desc = desc;
        this.model = model;
        this.visionModel = visionModel;
        this.visionLangModel = visionLangModel;
        this.transportConfig= transportConfig;
        this.metricSocketConfig = metricSocketConfig;
        this.agentSocketConfig = agentSocketConfig;
        this.taskMetricConfig = taskMetricConfig;
        this.actionMetricConfig = actionMetricConfig;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LangModelConfig getModel() {
        return model;
    }

    public void setModel(LangModelConfig model) {
        this.model = model;
    }

    public VisionModelConfig getVisionModel() {
        return visionModel;
    }

    public void setVisionModel(VisionModelConfig visionModel) {
        this.visionModel = visionModel;
    }

    public VisionLangModelConfig getVisionLangModel() {
        return visionLangModel;
    }

    public void setVisionLangModel(VisionLangModelConfig visionLangModel) {
        this.visionLangModel = visionLangModel;
    }

    public TransportConfig<?> getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(TransportConfig<?> transportConfig) {
        this.transportConfig = transportConfig;
    }

    public MetricSocketConfig getMetricSocketConfig() {
        return metricSocketConfig;
    }

    public void setMetricSocketConfig(MetricSocketConfig metricSocketConfig) {
        this.metricSocketConfig = metricSocketConfig;
    }

    public AgentSocketConfig getAgentSocketConfig() {
        return agentSocketConfig;
    }

    public void setAgentSocketConfig(AgentSocketConfig agentSocketConfig) {
        this.agentSocketConfig = agentSocketConfig;
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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

}
