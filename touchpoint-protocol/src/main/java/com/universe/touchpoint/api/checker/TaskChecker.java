package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContext;
import com.universe.touchpoint.driver.ActionGraph;

public interface TaskChecker<I extends TouchPoint, O> {

    O run(I touchPoint, ActionGraph actionGraph, TouchPointContext context);

}
