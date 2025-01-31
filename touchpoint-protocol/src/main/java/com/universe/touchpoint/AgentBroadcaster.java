package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.driver.AIModelBroadcaster;
import com.universe.touchpoint.transport.TouchPointTransportConfigBroadcaster;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AgentBroadcaster<C> implements TouchPointRegistry {

    public static AgentBroadcaster<?> INSTANCE;
    private static final Object lock = new Object();

    private static final Map<String, Class<?>> broadCasterMap = new HashMap<>();
    static {
        broadCasterMap.put("aiModel", AIModelBroadcaster.class);
        broadCasterMap.put("transportConfig", TouchPointTransportConfigBroadcaster.class);
    }

    public static <C> AgentBroadcaster<C> getInstance(String type) {
        synchronized (lock) {
            if (INSTANCE == null) {
                try {
                    INSTANCE = (AgentBroadcaster<C>) Objects.requireNonNull(
                            broadCasterMap.get(type)).getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating broadcaster for type: " + type, e);
                }
            }
            return (AgentBroadcaster<C>) INSTANCE;
        }
    }

    public abstract void send(C config, Context context);

}
