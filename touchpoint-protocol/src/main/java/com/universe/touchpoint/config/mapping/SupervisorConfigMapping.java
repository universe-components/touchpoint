package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.Supervisor;
import com.universe.touchpoint.config.SupervisorConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SupervisorConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(Supervisor.class, SupervisorConfig.class);
    }

}
