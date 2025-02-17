package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.driver.ActionGraph;

public interface ActionGraphChecker<I extends TouchPoint, O> {

    O run(I touchPoint, ActionGraph actionGraph);

}
