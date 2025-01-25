package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.Model;

public record AgentActionMeta(String name, Class<? extends TouchPoint> inputClass, Model model) {
}
