package com.universe.touchpoint.agent;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.api.executor.ImageEncoder;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.utils.ClassUtils;

public class AgentActionMetaInfo {

    private String actionName;
    private String className;
    private String desc;
    private ActionRole role;
    private ActionType type;
    private String inputClassName;
    private String outputClassName;
    private LangModelConfig model;
    private VisionModelConfig visionModel;
    private VisionLangModelConfig visionLangModel;
    private TransportConfig<?> transportConfig;
    private ActionMetricConfig actionMetricConfig;
    private ActionDependency toActions;

    public AgentActionMetaInfo(String actionName) {
        this.actionName = actionName;
    }

    public <C> AgentActionMetaInfo(String actionName, String className, String actionDesc, ActionRole role, String inputClassName, String outputClassName, LangModelConfig model, VisionModelConfig visionModelConfig, VisionLangModelConfig visionLangModelConfig, TransportConfig<C> transportConfig, ActionMetricConfig actionMetricConfig, ActionDependency toActions) {
        this.actionName = actionName;
        this.className = className;
        this.desc = actionDesc;
        this.role = role;
        try {
            if (ClassUtils.implementsInterface(Class.forName(className), ImageEncoder.class)) {
                this.type = ActionType.SENSOR;
            } else {
                this.type = ActionType.INPUT;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.inputClassName = inputClassName;
        this.outputClassName = outputClassName;
        this.model = model;
        this.visionModel = visionModelConfig;
        this.visionLangModel = visionLangModelConfig;
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

    public ActionType getType() {
        return type;
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
