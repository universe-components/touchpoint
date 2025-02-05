package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;

public record AgentActionMetaInfo(
        String actionName,
        String className,
        String desc,
        Class<? extends TouchPoint> inputClass,
        AIModelConfig model,
        TransportConfig<?> transportConfig) {
}
