package com.universe.touchpoint;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.AgentSocketConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.CoordinatorConfig;
import com.universe.touchpoint.config.SupervisorConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.AIModelConfigMapping;
import com.universe.touchpoint.config.mapping.CoordinatorConfigMapping;
import com.universe.touchpoint.config.mapping.SupervisorConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.utils.AnnotationUtils;

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
                AIModelConfig aiModelConfig = (AIModelConfig) AnnotationUtils.annotation2Config(
                                                                    Class.forName(clazz),
                                                                    AIModelConfigMapping.annotation2Config);

                /*
                 * Local Registry
                 */
                boolean coordinatorResult = registerCoordinator(Class.forName(clazz), (String) properties.get(0));
                boolean supervisorResult = registerSupervisor(Class.forName(clazz), (String) properties.get(0));
                ActionRole role = coordinatorResult ? ActionRole.COORDINATOR : (supervisorResult ? ActionRole.SUPERVISOR : null);
                AgentActionManager.getInstance().extractAndRegisterAction(
                            clazz,
                            aiModelConfig,
                            new TransportConfig<>(
                                    transportType,
                                    transportConfig),
                            (String) properties.get(0),
                            (String) properties.get(1),
                            role,
                            Agent.getName(),
                            (String[]) properties.get(4));
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
            RoleExecutorFactory.getInstance(coordinatorConfig.getTask())
                    .registerExecutor(actionName, actionClass.getConstructor().newInstance());
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
            RoleExecutorFactory.getInstance(supervisorConfig.getTask())
                    .registerExecutor(actionName, actionClass.getConstructor().newInstance());
            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void listenTasks(Context context, List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.second;  // 获取 List<Object>
            for (String task : (String[]) properties.get(4)) {
                TaskActionContext actionContext = new TaskActionContext((String) properties.get(0), task);

                AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(task);
                assert socketConfig != null;
                AgentSocketStateMachine.registerInstance(task, socketConfig.getBindProtocol());
                AgentSocketStateMachine.getInstance(task).socketProtocol().initialize(socketConfig);

                AgentSocketStateMachine.getInstance(task).registerReceiver(context, actionContext);
                DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
                AgentSocketStateMachine.getInstance(task).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(
                                AgentSocketState.PARTICIPANT_READY,
                                driverRegion.getTouchPointAction(actionContext.getAction())),
                        context,
                        task);
            }
        }
    }

}
