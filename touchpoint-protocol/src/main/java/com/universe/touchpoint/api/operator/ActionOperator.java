package com.universe.touchpoint.api.operator;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;

public interface ActionOperator<T extends TouchPoint> extends RoleExecutor<T, AgentAction<?, ?>> {
}
