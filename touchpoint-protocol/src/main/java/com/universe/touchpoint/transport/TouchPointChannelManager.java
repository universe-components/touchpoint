package com.universe.touchpoint.transport;

import android.content.Context;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.TouchPointAction;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
import com.universe.touchpoint.router.AgentRouter;
import com.universe.touchpoint.transport.broadcast.TouchPointBroadcastChannel;
import com.universe.touchpoint.transport.rpc.TouchPointDubboChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouchPointChannelManager {

    private static final Map<Transport, Class<?>> channelMapping = new HashMap<>();

    static {
        channelMapping.put(Transport.DUBBO, TouchPointDubboChannel.class);
    }

    public static TouchPointChannel defaultChannel(Context context) {
        try {
            return new TouchPointBroadcastChannel(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <A, C> TouchPointChannel selectChannel(A action, Context context) {
        if (action instanceof AgentAction) {
            Transport transport = ((AgentAction) action).getMeta().transportConfig().transportType();
            C config = (C) ((AgentAction) action).getMeta().transportConfig().config();

            if (transport == null) {
                return new TouchPointBroadcastChannel(context);
            }

            if (channelMapping.containsKey(transport)) {
                try {
                    return (TouchPointChannel) Objects.requireNonNull(
                            channelMapping.get(transport))
                            .getConstructor(Context.class, config.getClass())
                            .newInstance(context, config);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return new TouchPointBroadcastChannel(context);
    }

    public static void registerContextReceiver(String[] filters, AgentActionMetaInfo agentActionMetaInfo) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(agentActionMetaInfo.name());
            TouchPointAction tpInstanceReceiver = (TouchPointAction) tpInstanceReceiverClass.getConstructor().newInstance();
            registerContextReceiver(filters, tpInstanceReceiver);
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

    private static void registerContextReceiver(String[] filters, TouchPointAction tpInstanceReceiver) {
        if (filters != null) {
            for (String filter : filters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
                transportRegion.putTouchPointReceiver(filterAction, tpInstanceReceiver);
            }
        }
    }

}
