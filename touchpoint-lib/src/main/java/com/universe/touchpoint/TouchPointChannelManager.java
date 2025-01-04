package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.channel.broadcast.TouchPointBroadcastChannel;
import com.universe.touchpoint.channel.eventbus.TouchPointEventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouchPointChannelManager {

    private static final Object lock = new Object();
    private static final Map<String, Class<?>> channelMapping = new HashMap<>();

    static {
        channelMapping.put("broadcast", TouchPointBroadcastChannel.class);
        channelMapping.put("eventbus", TouchPointEventBus.class);
    }

    public static TouchPointChannel defaultChannel(Context context) {
        try {
            return new TouchPointBroadcastChannel(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static TouchPointChannel selectChannel(String type) {
        synchronized (lock) {
            try {
                return (TouchPointChannel) Objects.requireNonNull(channelMapping.get(type)).getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
