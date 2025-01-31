package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.Transport;
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

}
