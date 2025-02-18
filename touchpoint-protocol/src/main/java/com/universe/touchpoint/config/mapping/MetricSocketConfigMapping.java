package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.metric.MetricSocket;
import com.universe.touchpoint.config.metric.MetricSocketConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class MetricSocketConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(MetricSocket.class, MetricSocketConfig.class);
    }

}
