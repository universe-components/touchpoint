package com.universe.touchpoint.agent;

import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;

public record AgentActionMetaInfo(
        String actionName,
        String className,
        String desc,
        String state,
        ActionRole role,
        String inputClassName,
        String outputClassName,
        AIModelConfig model,
        TransportConfig<?> transportConfig) {
}
