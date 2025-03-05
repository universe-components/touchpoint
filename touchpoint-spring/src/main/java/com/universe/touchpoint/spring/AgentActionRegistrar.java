package com.universe.touchpoint.spring;

import com.universe.touchpoint.TaskParticipant;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.Coordinator;
import com.universe.touchpoint.annotations.role.Supervisor;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.monitor.MetricSyncerFactory;
import com.universe.touchpoint.meta.annotation.ActionAnnotationMeta;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.context.TaskActionContext;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.util.Map;
import javax.annotation.Nonnull;

public class AgentActionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata importingClassMetadata, @Nonnull BeanDefinitionRegistry registry) {
        Class<?> actionClass = ClassUtils.resolveClassName(importingClassMetadata.getClassName(), null);
        if (importingClassMetadata.hasAnnotation(TouchPointAction.class.getName())) {
            String actionClassName = TouchPointAction.class.getName();
            Map<String, Object> actionAttributes = importingClassMetadata.getAnnotationAttributes(actionClassName);
            assert actionAttributes != null;
            String actionName = (String) actionAttributes.get("name");
            String actionDesc = (String) actionAttributes.get("desc");
            ActionAnnotationMeta actionAnnotationMeta = new ActionAnnotationMeta(actionClass, actionAttributes);
            boolean isSupervisor = false;
            boolean isCoordinator = false;

            if (importingClassMetadata.hasAnnotation(Supervisor.class.getName())) {
                isSupervisor = TaskParticipant.registerSupervisor(actionClass, actionName);
            }
            if (importingClassMetadata.hasAnnotation(Coordinator.class.getName())) {
                isCoordinator = TaskParticipant.registerCoordinator(actionClass, actionName);
            }
            ActionRole role = isCoordinator ? ActionRole.COORDINATOR : (isSupervisor ? ActionRole.SUPERVISOR : ActionRole.EXECUTOR);

            try {
                assert actionName != null;
                AgentActionMeta actionMeta = AgentActionManager.getInstance().buildAction(
                        actionClassName,
                        environment.getProperty("spring.application.name", "default"),
                        actionAnnotationMeta.getLangModel(),
                        actionAnnotationMeta.getVisionModel(),
                        actionAnnotationMeta.getVisionLangModel(),
                        actionAnnotationMeta.getTransportConfig(),
                        actionName,
                        actionDesc,
                        role,
                        actionAnnotationMeta.getActionMetricConfig(),
                        actionAnnotationMeta.getActionDependency());
                ((MetaRegion) TouchPointMemory.getRegion(Region.META)).putTouchPointAction(actionName, actionMeta);

                for (String task : actionAnnotationMeta.getActionDependency().getTasks()) {
                    AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
                    assert socketConfig != null;
                    AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
                    AgentSocketStateMachine.getInstance(task).socketProtocol().initialize(socketConfig);
                    AgentSocketStateMachine.getInstance(task).registerReceiver(new TaskActionContext(actionName, task), role);

                    MetricSocketConfig metricSocketConfig = ConfigManager.selectMetricSocket(task);
                    assert metricSocketConfig != null;
                    MetricSyncerFactory.registerSyncer(task, metricSocketConfig.getBindProtocol()).initialize(metricSocketConfig);
                    MetricSyncerFactory.getSyncer(task).registerListener(task);

                    AgentSocketStateMachine.getInstance(task).send(
                            new AgentSocketStateMachine.AgentSocketStateContext<>(
                                    AgentSocketState.PARTICIPANT_READY,
                                    actionMeta),
                            TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, task, ActionRole.EXECUTOR.name()));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
