package com.universe.touchpoint.api.operator;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContext;
import com.universe.touchpoint.agent.AgentAction;

public interface ActionOperator<T extends TouchPoint> {

    AgentAction<?, ?> run(T condition, TouchPointContext context);

}
