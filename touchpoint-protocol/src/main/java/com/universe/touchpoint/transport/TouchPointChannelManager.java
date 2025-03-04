package com.universe.touchpoint.transport;

import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.transport.mqtt.TouchPointMQTT5Publisher;
import com.universe.touchpoint.transport.rpc.TouchPointDubboChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouchPointChannelManager {

    private static final Map<Transport, Class<?>> channelMapping = new HashMap<>();

    static {
        channelMapping.put(Transport.DUBBO, TouchPointDubboChannel.class);
        channelMapping.put(Transport.MQTT, TouchPointMQTT5Publisher.class);
    }

    public static TouchPointChannel<?> defaultChannel() {
        try {
            return new TouchPointMQTT5Publisher();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <C> TouchPointChannel<?> selectChannel(AgentActionMeta actionMeta, String task) {
        TransportConfig<C> transportConfig = ConfigManager.selectTransport(actionMeta.getName(), task);
        Transport transport = transportConfig.transportType();
        C config = transportConfig.config();

        if (channelMapping.containsKey(transport)) {
            try {
                if (config instanceof RPCConfig) {
                    return (TouchPointChannel<?>) Objects.requireNonNull(
                                    channelMapping.get(transport))
                                    .getConstructor(config.getClass())
                                    .newInstance(config);
                }
                return (TouchPointChannel<?>) Objects.requireNonNull(
                        channelMapping.get(transport))
                        .getConstructor()
                        .newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return new TouchPointMQTT5Publisher();
    }

    public static void registerContextReceiver(String actionName, String actionClassName, String task) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(actionClassName);
            RoleExecutor<?, ?> tpInstanceReceiver = (RoleExecutor<?, ?>) tpInstanceReceiverClass.getConstructor().newInstance();
            TaskRoleExecutor.getInstance(task).registerExecutor(actionName, tpInstanceReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
