package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;

public interface ActionChecker<I extends TouchPoint, O> {

    O run(I touchPoint, AgentAction<?, ?> action);

}
