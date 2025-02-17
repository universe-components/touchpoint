package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.task.MonitorMetrics;
import com.universe.touchpoint.config.task.ActionMetricConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ActionMetricConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(MonitorMetrics.class, ActionMetricConfig.class);
    }

}
