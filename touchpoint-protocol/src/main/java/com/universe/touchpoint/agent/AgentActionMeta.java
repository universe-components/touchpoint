package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.AIModelConfig;

public record AgentActionMeta(String name, Class<? extends TouchPoint> inputClass, AIModelConfig model) {
}
