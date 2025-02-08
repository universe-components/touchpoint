package com.universe.touchpoint.agent;

import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;

public record AgentActionMetaInfo(
        String actionName,
        String className,
        String desc,
        String inputClassName,
        String outputClassName,
        AIModelConfig model,
        TransportConfig<?> transportConfig) {
}
