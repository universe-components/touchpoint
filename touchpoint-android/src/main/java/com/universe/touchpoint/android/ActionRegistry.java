package com.universe.touchpoint.android;

import com.universe.touchpoint.TaskParticipant;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.mapping.ActionMetricConfigMapping;
import com.universe.touchpoint.config.mapping.LangModelConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.config.mapping.VisionLangModelConfigMapping;
import com.universe.touchpoint.config.mapping.VisionModelConfigMapping;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.role.CoordinatorConfig;
import com.universe.touchpoint.config.role.SupervisorConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.MetaManager;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.meta.data.RoleModel;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.StringUtils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class ActionRegistry {

    public static void registerActions(List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            String clazz = pair.getLeft();  // 获取 String
            List<Object> properties = pair.getRight();  // 获取 List<Object>

            Map<Transport, Object> transportConfigMap;
            try {
                transportConfigMap = AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        TransportConfigMapping.annotation2Config,
                        TransportConfigMapping.annotation2Type
                );

                Transport transportType = transportConfigMap.keySet().iterator().next();
                Object transportConfig = transportConfigMap.get(transportType);
                LangModelConfig langModelConfig = (LangModelConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        LangModelConfigMapping.annotation2Config);
                VisionModelConfig visionModelConfig = (VisionModelConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        VisionModelConfigMapping.annotation2Config);
                VisionLangModelConfig visionLangModelConfig = (VisionLangModelConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        VisionLangModelConfigMapping.annotation2Config);
                ActionMetricConfig actionMetricConfig = (ActionMetricConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        ActionMetricConfigMapping.annotation2Config);

                /*
                 * Local Registry
                 */
                String scopeAction = (String) properties.get(4);
                RoleModel<?> roleModel = new RoleModel<>((ActionRole) properties.get(2), null);
                CoordinatorConfig coordinatorResult = TaskParticipant.registerCoordinator(Class.forName(clazz), (String) properties.get(0));
                if (coordinatorResult != null) {
                    roleModel = new RoleModel<>(ActionRole.COORDINATOR, coordinatorResult);
                }
                SupervisorConfig supervisorResult = TaskParticipant.registerSupervisor(Class.forName(clazz), (String) properties.get(0));
                if (supervisorResult != null) {
                    roleModel = new RoleModel<>(ActionRole.SUPERVISOR, supervisorResult);
                }

                ActionDependency actionDependency = new ActionDependency((String) properties.get(0));
                actionDependency.setToActions(StringUtils.convert((String[]) properties.get(3)));
                ((MetaRegion) TouchPointMemory.getRegion(Region.META)).putTouchPointAction((String) properties.get(0), MetaManager.buildAction(
                        clazz,
                        Agent.getName(),
                        langModelConfig,
                        visionModelConfig,
                        visionLangModelConfig,
                        new TransportConfig<>(
                                transportType,
                                transportConfig),
                        (String) properties.get(0),
                        (String) properties.get(1),
                        roleModel,
                        actionMetricConfig,
                        actionDependency,
                        scopeAction));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void listenTasks(List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.getRight();  // 获取 List<Object>
            Map<String, List<String>> toActions = StringUtils.convert((String[]) properties.get(3));
            for (String task : toActions.keySet()) {
                TaskActionContext actionContext = new TaskActionContext((String) properties.get(0), task);
                MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
                AgentActionMeta actionMeta = metaRegion.getTouchPointAction(actionContext.getAction());

                AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
                assert socketConfig != null;
                AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
                AgentSocketStateMachine.getInstance(task).getSocketProtocol().initialize(socketConfig);
                AgentSocketStateMachine.getInstance(task).registerReceiver(actionContext, actionMeta.getRoleType());

                MetricSocketConfig metricSocketConfig = ConfigManager.selectMetricSocket(task);
                assert metricSocketConfig != null;
                ((AgentSyncProtocol<Pair>) AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol())).registerReceiver(
                        actionContext,
                        TouchPointConstants.METRIC_FILTER, RoleType.MEMBER, Pair.class);


                AgentSocketStateMachine.getInstance(task).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(
                                AgentSocketState.ACTION_GRAPH_READY,
                                actionMeta),
                        TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, task, RoleType.OWNER.name()));
            }
        }
    }

}