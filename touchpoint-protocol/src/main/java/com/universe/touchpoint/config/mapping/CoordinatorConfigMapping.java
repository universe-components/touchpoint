package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.role.Coordinator;
import com.universe.touchpoint.config.CoordinatorConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class CoordinatorConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(Coordinator.class, CoordinatorConfig.class);
    }

}
