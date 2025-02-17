package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.metric.MonitorTaskMetrics;
import com.universe.touchpoint.config.metric.TaskMetricConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class TaskMetricConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(MonitorTaskMetrics.class, TaskMetricConfig.class);
    }

}
