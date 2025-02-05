package com.universe.touchpoint;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.AIModelConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.router.AgentRouter;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;
import com.universe.touchpoint.utils.AnnotationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskParticipant {

    public static void registerActions(Context appContext, List<Pair<String, List<Object>>> receiverFilterPair, boolean isPlugin, ConfigType configType) {
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
                String[] filters = Stream.of((String[]) properties.get(1), (String[]) properties.get(2)).flatMap(Stream::of).toArray(String[]::new);

                /*
                 * Local Registry
                 */
                AgentActionMetaInfo actionMetaInfo = AgentActionManager.getInstance()
                        .extractAndRegisterAction(
                            clazz,
                            aiModelConfig,
                            new TransportConfig<>(
                                    transportType,
                                    transportConfig),
                            (String) properties.get(0),
                            Agent.getName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    AgentActionManager.getInstance()
                            .registerAgentFinishReceiver(
                                appContext,
                                filters,
                                actionMetaInfo.inputClass());
                }

                TouchPointTransportRegistryFactory
                        .createRegistry(actionMetaInfo.transportConfig().transportType())
                        .register(
                                appContext,
                                actionMetaInfo,
                                filters
                        );

                TouchPointChannelManager.registerContextReceiver(filters, actionMetaInfo);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void listenRoutes(Context context, List<Pair<String, List<Object>>> receiverFilterPair) {
//        AgentBroadcaster.getInstance("aiModel").registerReceiver(context, Agent.getName());
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.second;
            for (String task : (String[]) properties.get(3)) {
                ArrayList<String> routeEntries = Arrays.stream((String[]) pair.second.get(1))
                        .map(agent -> AgentRouter.buildChunk(agent, Agent.getName()))
                        .collect(Collectors.toCollection(ArrayList::new));
                TaskActionContext actionContext = new TaskActionContext(pair.first, task, routeEntries);
                AgentReporter.getInstance("router").registerReceiver(context, actionContext);
            }
        }
    }

    public static void listenTasks(Context context, List<Pair<String, List<Object>>> receiverFilterPair) {
        for (Pair<String, List<Object>> pair : receiverFilterPair) {
            List<Object> properties = pair.second;  // 获取 List<Object>
            for (String task : (String[]) properties.get(3)) {
                TaskActionContext actionContext = new TaskActionContext(pair.first, task);
                AgentSocketStateMachine.getInstance().registerReceiver(context, actionContext);
//                AgentBroadcaster.getInstance("transportConfig").registerReceiver(context, actionContext);
            }
        }
    }

}
