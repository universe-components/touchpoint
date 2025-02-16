package com.universe.touchpoint.agent;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.transport.TransportConfig;

public record AgentActionMetaInfo(
        String actionName,
        String className,
        String desc,
        ActionRole role,
        String inputClassName,
        String outputClassName,
        AIModelConfig model,
        TransportConfig<?> transportConfig,
        String[] toActions) {
}
