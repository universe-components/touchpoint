package com.universe.touchpoint.transport;

import android.content.Context;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.RPCConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
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

    public static void registerContextReceiver(String actionName, String actionClassName) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(actionClassName);
            TouchPointListener<?, ?> tpInstanceReceiver = (TouchPointListener<?, ?>) tpInstanceReceiverClass.getConstructor().newInstance();
            registerContextReceiver(actionName, tpInstanceReceiver);
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

    private static void registerContextReceiver(String actionName, TouchPointListener<?, ?> tpInstanceReceiver) {
        String filterAction = TouchPointHelper.touchPointFilterName(actionName);
        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        transportRegion.putTouchPointReceiver(filterAction, tpInstanceReceiver);
    }

}
