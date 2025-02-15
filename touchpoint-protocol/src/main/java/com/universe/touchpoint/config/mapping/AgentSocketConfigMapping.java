package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.socket.AgentSocket;
import com.universe.touchpoint.config.AgentSocketConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AgentSocketConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(AgentSocket.class, AgentSocketConfig.class);
    }

}
