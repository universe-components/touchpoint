package com.universe.touchpoint.agent;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;

public class AgentActionMetaInfo {

    private String actionName;
    private String className;
    private String desc;
    private ActionRole role;
    private String inputClassName;
    private String outputClassName;
    private AIModelConfig model;
    private TransportConfig<?> transportConfig;
    private ActionMetricConfig actionMetricConfig;
    private ActionDependency toActions;

    public AgentActionMetaInfo(String actionName) {
        this.actionName = actionName;
    }

    public <C> AgentActionMetaInfo(String actionName, String receiverClassName, String actionDesc, ActionRole role, String inputClassName, String outputClassName, AIModelConfig model, TransportConfig<C> transportConfig, ActionMetricConfig actionMetricConfig, ActionDependency toActions) {
        this.actionName = actionName;
        this.className = receiverClassName;
        this.desc = actionDesc;
        this.role = role;
        this.inputClassName = inputClassName;
        this.outputClassName = outputClassName;
        this.model = model;
        this.transportConfig = transportConfig;
        this.actionMetricConfig = actionMetricConfig;
        this.toActions = toActions;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
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

    public ActionRole getRole() {
        return role;
    }

    public void setRole(ActionRole role) {
        this.role = role;
    }

    public String getInputClassName() {
        return inputClassName;
    }

    public void setInputClassName(String inputClassName) {
        this.inputClassName = inputClassName;
    }

    public String getOutputClassName() {
        return outputClassName;
    }

    public void setOutputClassName(String outputClassName) {
        this.outputClassName = outputClassName;
    }

    public AIModelConfig getModel() {
        return model;
    }

    public void setModel(AIModelConfig model) {
        this.model = model;
    }

    public TransportConfig<?> getTransportConfig() {
        return transportConfig;
    }

    public void setTransportConfig(TransportConfig<?> transportConfig) {
        this.transportConfig = transportConfig;
    }

    public ActionMetricConfig getActionMetricConfig() {
        return actionMetricConfig;
    }

    public void setActionMetricConfig(ActionMetricConfig actionMetricConfig) {
        this.actionMetricConfig = actionMetricConfig;
    }

    public ActionDependency getToActions() {
        return toActions;
    }

    public void setToActions(ActionDependency toActions) {
        this.toActions = toActions;
    }

}
