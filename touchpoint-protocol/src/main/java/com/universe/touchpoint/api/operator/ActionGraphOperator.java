package com.universe.touchpoint.api.operator;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.plan.ActionGraph;

public interface ActionGraphOperator<T extends TouchPoint> extends RoleExecutor<T, ActionGraph> {
}
