package com.universe.touchpoint.api.checker.defaults;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.checker.ActionGraphChecker;
import com.universe.touchpoint.driver.ActionGraph;

public interface DefaultActionGraphChecker<T extends TouchPoint> extends ActionGraphChecker<T, Boolean> {

    Boolean run(T touchPoint, ActionGraph actionGraph);

}
