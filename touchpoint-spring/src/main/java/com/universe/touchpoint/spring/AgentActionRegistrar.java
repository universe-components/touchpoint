package com.universe.touchpoint.spring;

import com.universe.touchpoint.TaskParticipant;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.Coordinator;
import com.universe.touchpoint.annotations.role.OperateType;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.role.Supervisor;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.role.CoordinatorConfig;
import com.universe.touchpoint.config.role.SupervisorConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.MetaManager;
import com.universe.touchpoint.meta.annotation.ActionAnnotationMeta;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.meta.data.RoleModel;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.Map;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

public class AgentActionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

  private Environment environment;

  @Override
  public void setEnvironment(@Nonnull Environment environment) {
    this.environment = environment;
  }

  @Override
  public void registerBeanDefinitions(
      @Nonnull AnnotationMetadata importingClassMetadata,
      @Nonnull BeanDefinitionRegistry registry) {
    Class<?> actionClass = ClassUtils.resolveClassName(importingClassMetadata.getClassName(), null);
    if (importingClassMetadata.hasAnnotation(TouchPointAction.class.getName())) {
      String actionClassName = TouchPointAction.class.getName();
      Map<String, Object> actionAttributes =
          importingClassMetadata.getAnnotationAttributes(actionClassName);
      assert actionAttributes != null;
      ActionAnnotationMeta actionAnnotationMeta =
          new ActionAnnotationMeta(actionClass, actionAttributes);

      RoleModel<?> roleModel = new RoleModel<>(actionAnnotationMeta.getRole(), null);
      String scopeAction = actionAnnotationMeta.getScopeAction();
      if (importingClassMetadata.hasAnnotation(Supervisor.class.getName())) {
        TaskParticipant.registerSupervisor(actionClass, actionAnnotationMeta.getName());
        Map<String, Object> supervisorAttributes =
            importingClassMetadata.getAnnotationAttributes(Supervisor.class.getName());
        assert supervisorAttributes != null;
        roleModel =
            new RoleModel<>(
                ActionRole.SUPERVISOR,
                new SupervisorConfig(
                    (String) supervisorAttributes.get("task"),
                    (String) supervisorAttributes.get("scopeAction")));
      }
      if (importingClassMetadata.hasAnnotation(Coordinator.class.getName())) {
        TaskParticipant.registerCoordinator(actionClass, actionAnnotationMeta.getName());
        Map<String, Object> coordinatorAttributes =
            importingClassMetadata.getAnnotationAttributes(Coordinator.class.getName());
        assert coordinatorAttributes != null;
        roleModel =
            new RoleModel<>(
                ActionRole.COORDINATOR,
                new CoordinatorConfig(
                    (String) coordinatorAttributes.get("task"),
                    (String) coordinatorAttributes.get("scope"),
                    (OperateType) coordinatorAttributes.get("operateType")));
      }

      try {
        AgentActionMeta actionMeta =
            MetaManager.buildAction(
                actionClassName,
                environment.getProperty("spring.application.name", "default"),
                actionAnnotationMeta.getLangModel(),
                actionAnnotationMeta.getVisionModel(),
                actionAnnotationMeta.getVisionLangModel(),
                actionAnnotationMeta.getTransportConfig(),
                actionAnnotationMeta.getName(),
                actionAnnotationMeta.getDescription(),
                roleModel,
                actionAnnotationMeta.getActionMetricConfig(),
                actionAnnotationMeta.getActionDependency(),
                scopeAction);
        ((MetaRegion) TouchPointMemory.getRegion(Region.META))
            .putTouchPointAction(actionAnnotationMeta.getName(), actionMeta);

        // Todo：no task action register to `all` filter for not limited to state machine etc.

        for (String task : actionAnnotationMeta.getActionDependency().getTasks()) {
          AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
          assert socketConfig != null;
          AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
          AgentSocketStateMachine.getInstance(task).getSocketProtocol().initialize(socketConfig);
          assert actionMeta != null;
          AgentSocketStateMachine.getInstance(task)
              .registerReceiver(
                  new TaskActionContext(actionAnnotationMeta.getName(), task),
                  actionMeta.getRoleType());

          MetricSocketConfig metricSocketConfig = ConfigManager.selectMetricSocket(task);
          assert metricSocketConfig != null;
          ((AgentSyncProtocol<Pair>)
                  AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol()))
              .registerReceiver(
                  new TaskActionContext(actionAnnotationMeta.getName(), task),
                  TouchPointConstants.METRIC_FILTER,
                  RoleType.MEMBER,
                  Pair.class);

          AgentSocketStateMachine.getInstance(task)
              .send(
                  new AgentSocketStateMachine.AgentSocketStateContext<>(
                      AgentSocketState.ACTION_GRAPH_READY, actionMeta),
                  TouchPointHelper.touchPointFilterName(
                      TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                      task,
                      RoleType.OWNER.name()));
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
