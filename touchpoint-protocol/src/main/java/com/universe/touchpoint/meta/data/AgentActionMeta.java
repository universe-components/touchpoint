package com.universe.touchpoint.meta.data;

import com.universe.touchpoint.agent.ActionType;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.api.executor.ActionPredictor;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.meta.BaseMeta;
import com.universe.touchpoint.utils.ClassUtils;

public class AgentActionMeta extends BaseMeta {

    private ActionRole role;
    private RoleType roleType;
    private ActionType type;
    private String inputClassName;
    private String outputClassName;
    private ActionDependency toActions;
    private ActionMetricConfig actionMetricConfig;

    public AgentActionMeta(String actionName) {
        super(actionName);
    }

    public <C> AgentActionMeta(
            String actionName,
            String agentName,
            String className,
            String actionDesc,
            ActionRole role,
            String inputClassName,
            String outputClassName,
            LangModelConfig model,
            VisionModelConfig visionModelConfig,
            VisionLangModelConfig visionLangModelConfig,
            TransportConfig<C> transportConfig,
            ActionMetricConfig actionMetricConfig,
            ActionDependency toActions) {
        super(
            actionName,
            agentName,
            className,
            actionDesc,
            model,
            visionModelConfig,
            visionLangModelConfig,
            transportConfig,
            null,
            null,
            null,
            actionMetricConfig
        );
        this.role = role;
        this.roleType = role == ActionRole.PROPOSER ? RoleType.OWNER : RoleType.MEMBER;
        try {
            if (ClassUtils.extendsAbstractClass(Class.forName(className), ActionPredictor.class)) {
                this.type = ActionType.ACT;
            } else {
                this.type = ActionType.CHAT;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.inputClassName = inputClassName;
        this.outputClassName = outputClassName;
        this.toActions = toActions;
    }

    public ActionRole getRole() {
        return role;
    }

    public void setRole(ActionRole role) {
        this.role = role;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
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

    public ActionDependency getToActions() {
        return toActions;
    }

    public void setToActions(ActionDependency toActions) {
        this.toActions = toActions;
    }

    @Override
    public ActionMetricConfig getActionMetricConfig() {
        return actionMetricConfig;
    }

    @Override
    public void setActionMetricConfig(ActionMetricConfig actionMetricConfig) {
        this.actionMetricConfig = actionMetricConfig;
    }

}
