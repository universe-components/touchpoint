package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.transport.broadcast.TouchPointBroadcastChannel;
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

    public static TouchPointChannel<?> defaultChannel(Context context) {
        try {
            return new TouchPointBroadcastChannel(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <C> TouchPointChannel<?> selectChannel(AgentActionMetaInfo actionMeta, Context context) {
        Transport transport = actionMeta.getTransportConfig().transportType();
        C config = (C) actionMeta.getTransportConfig().config();

        if (transport == null) {
            return new TouchPointBroadcastChannel(context);
        }

        if (channelMapping.containsKey(transport)) {
            try {
                if (config instanceof RPCConfig) {
                    return (TouchPointChannel<?>) Objects.requireNonNull(
                                    channelMapping.get(transport))
                                    .getConstructor(Context.class, config.getClass())
                                    .newInstance(context, config);
                }
                return (TouchPointChannel<?>) Objects.requireNonNull(
                        channelMapping.get(transport))
                        .getConstructor(Context.class)
                        .newInstance(context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return new TouchPointBroadcastChannel(context);
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
