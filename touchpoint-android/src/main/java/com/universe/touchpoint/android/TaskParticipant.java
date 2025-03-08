package com.universe.touchpoint.android;

import android.util.Pair;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.mapping.ActionMetricConfigMapping;
import com.universe.touchpoint.config.mapping.CoordinatorConfigMapping;
import com.universe.touchpoint.config.mapping.LangModelConfigMapping;
import com.universe.touchpoint.config.mapping.SupervisorConfigMapping;
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
import com.universe.touchpoint.monitor.MetricSyncerFactory;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.context.TaskActionContext;
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.StringUtils;

import java.util.List;
import java.util.Map;

public class TaskParticipant {

    public static void registerActions(List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            String clazz = pair.first;  // 获取 String
            List<Object> properties = pair.second;  // 获取 List<Object>

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
                boolean coordinatorResult = registerCoordinator(Class.forName(clazz), (String) properties.get(0));
                boolean supervisorResult = registerSupervisor(Class.forName(clazz), (String) properties.get(0));
                ActionRole role = coordinatorResult ? ActionRole.COORDINATOR : (supervisorResult ? ActionRole.SUPERVISOR : ActionRole.EXECUTOR);

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
                        role,
                        actionMetricConfig,
                        actionDependency));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static boolean registerCoordinator(Class<?> actionClass, String actionName) {
        try {
            CoordinatorConfig coordinatorConfig = (CoordinatorConfig) AnnotationUtils.annotation2Config(
                    actionClass,
                    CoordinatorConfigMapping.annotation2Config);
            if (coordinatorConfig == null) {
                return false;
            }
            TaskRoleExecutor.getInstance(coordinatorConfig.getTask())
                    .registerExecutor(actionName, (RoleExecutor<?, ?>) actionClass.getConstructor().newInstance());
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean registerSupervisor(Class<?> actionClass, String actionName) {
        try {
            SupervisorConfig supervisorConfig = (SupervisorConfig) AnnotationUtils.annotation2Config(
                    actionClass,
                    SupervisorConfigMapping.annotation2Config);
            if (supervisorConfig == null) {
                return false;
            }
            TaskRoleExecutor.getInstance(supervisorConfig.getTask())
                    .registerExecutor(actionName, (RoleExecutor<?, ?>) actionClass.getConstructor().newInstance());
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void listenTasks(List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.second;  // 获取 List<Object>
            Map<String, List<String>> toActions = StringUtils.convert((String[]) properties.get(3));
            for (String task : toActions.keySet()) {
                TaskActionContext actionContext = new TaskActionContext((String) properties.get(0), task);
                MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
                AgentActionMeta actionMeta = metaRegion.getTouchPointAction(actionContext.getAction());

                AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
                assert socketConfig != null;
                AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
                AgentSocketStateMachine.getInstance(task).socketProtocol().initialize(socketConfig);
                AgentSocketStateMachine.getInstance(task).registerReceiver(actionContext, actionMeta.getRoleType());

                MetricSocketConfig metricSocketConfig = ConfigManager.selectMetricSocket(task);
                assert metricSocketConfig != null;
                MetricSyncerFactory.registerSyncer(task, metricSocketConfig.getBindProtocol()).initialize(metricSocketConfig);
                MetricSyncerFactory.getSyncer(task).registerListener(task);


                AgentSocketStateMachine.getInstance(task).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(
                                AgentSocketState.ACTION_GRAPH_READY,
                                actionMeta),
                        TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, task, RoleType.OWNER.name()));
            }
        }
    }

}