package com.universe.touchpoint;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.api.ActionCoordinator;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.CoordinatorConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.AIModelConfigMapping;
import com.universe.touchpoint.config.mapping.CoordinatorConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.driver.CollaborationFactory;
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
                AgentActionManager.getInstance().extractAndRegisterAction(
                            clazz,
                            aiModelConfig,
                            new TransportConfig<>(
                                    transportType,
                                    transportConfig),
                            (String) properties.get(0),
                            (String) properties.get(1),
                            (ActionRole) properties.get(2),
                            Agent.getName());
                registerCollaboration(Class.forName(clazz), (String) properties.get(0));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void registerCollaboration(Class<?> actionClass, String actionName) {
        try {
            CoordinatorConfig coordinatorConfig = (CoordinatorConfig) AnnotationUtils.annotation2Config(
                    actionClass,
                    CoordinatorConfigMapping.annotation2Config);
            assert coordinatorConfig != null;
            CollaborationFactory.getInstance(coordinatorConfig.getTask())
                    .registerOperator(actionName, (ActionCoordinator) actionClass.getConstructor().newInstance());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void listenTasks(Context context, List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.second;  // 获取 List<Object>
            for (String task : (String[]) properties.get(4)) {
                TaskActionContext actionContext = new TaskActionContext((String) properties.get(0), task);
                AgentSocketStateMachine.getInstance().registerReceiver(context, actionContext);
            }
        }
    }

}
