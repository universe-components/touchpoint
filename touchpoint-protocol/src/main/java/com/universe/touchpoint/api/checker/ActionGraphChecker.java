package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.driver.ActionGraph;

public interface ActionGraphChecker<T extends TouchPoint> {

    boolean run(T touchPoint, ActionGraph actionGraph);

}
