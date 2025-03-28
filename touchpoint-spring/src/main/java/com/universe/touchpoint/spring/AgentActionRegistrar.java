package com.universe.touchpoint.spring;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.meta.MetaManager;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.annotation.ActionAnnotationMeta;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import com.universe.touchpoint.textmodel.TFIDF;
import com.universe.touchpoint.textmodel.Tokenizer;
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

      String scopeAction = actionAnnotationMeta.getScopeAction();
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
                actionAnnotationMeta.getOperateType(),
                actionAnnotationMeta.getActionMetricConfig(),
                actionAnnotationMeta.getActionDependency(),
                scopeAction);
        ((MetaRegion) TouchPointMemory.getRegion(Region.META))
            .putTouchPointAction(actionAnnotationMeta.getName(), actionMeta);

        for (String task : actionAnnotationMeta.getActionDependency().getTasks()) {
          AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
          assert socketConfig != null;
          AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
          AgentSocketStateMachine.getInstance(task).getSocketProtocol().initialize(socketConfig);
          assert actionMeta != null;

          new Tokenizer().loadStopWords("resources/stop_words.txt");

          TFIDF tfidf = new TFIDF();
          tfidf.loadIDF("resources/idf_dict.txt");
          tfidf.startHotIDFLoader(60);

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
          ((AgentSyncProtocol<SocketRequest>)
                  AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol()))
              .registerReceiver(
                  new TaskActionContext(actionAnnotationMeta.getName(), task),
                  TouchPointConstants.TOUCH_POINT_ACTIVITY_FILTER,
                  RoleType.MEMBER,
                  SocketRequest.class);

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
