package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;

public interface ActionChecker<T extends TouchPoint> {

    boolean run(T touchPoint, AgentAction<?, ?> action);

}
