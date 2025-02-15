package com.universe.touchpoint.api.operator;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.driver.ActionGraph;

public interface ActionGraphOperator<T extends TouchPoint> {

    ActionGraph run(T condition, ActionGraph actionGraph);

}
