package com.universe.touchpoint.api.checker.defaults;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContext;
import com.universe.touchpoint.api.checker.TaskChecker;
import com.universe.touchpoint.driver.ActionGraph;

public interface DefaultTaskChecker<T extends TouchPoint> extends TaskChecker<T, Boolean> {

    Boolean run(T touchPoint, ActionGraph actionGraph, TouchPointContext context);

}
