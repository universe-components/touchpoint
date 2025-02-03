package com.universe.touchpoint;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.TaskProposer;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.AIModelConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;
import com.universe.touchpoint.utils.AnnotationUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class AgentRegistry {

    private static final Object mLock = new Object();
    private static AgentRegistry mInstance;

    public static AgentRegistry getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AgentRegistry();
            }
            return mInstance;
        }
    }

    public void registerProposer() {
        if (Agent.isAnnotationPresent(TaskProposer.class)) {
            TransportConfig<?> transportConfigWrapper;
            try {
                transportConfigWrapper = (TransportConfig<?>) AnnotationUtils.annotation2Config(
                        Agent.getApplicationClass(), TransportConfigMapping.annotation2Config);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            AgentBuilder.getBuilder().getConfig().setTransportConfig(transportConfigWrapper);
        }
    }

    public void registerActions(Context appContext, List<Pair<String, List<Object>>> receiverFilterPair, boolean isPlugin, ConfigType configType) {
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
                        Class.forName(clazz), AIModelConfigMapping.annotation2Config);
                String[] filters = Stream.of((String[]) properties.get(1), (String[]) properties.get(2)).flatMap(Stream::of).toArray(String[]::new);

                /*
                 * Local Registry
                 */
                AgentActionMetaInfo actionMetaInfo = AgentActionManager.getInstance().extractAndRegisterAction(
                        clazz,
                        aiModelConfig,
                        new TransportConfig<>(
                                transportType,
                                transportConfig),
                        (String) properties.get(0),
                        Agent.getName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    AgentActionManager.getInstance().registerAgentFinishReceiver(
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

    public void registerReceivers(Context context) {
        AgentReporter.getInstance("taskAction").registerReceiver(context);
        AgentBroadcaster.getInstance("aiModel").registerReceiver(context);
        AgentBroadcaster.getInstance("transportConfig").registerReceiver(context);
        AgentReporter.getInstance("router").registerReceiver(context);
    }

}
