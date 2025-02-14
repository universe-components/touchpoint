package com.universe.touchpoint.api;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.driver.ActionGraph;

public interface ActionCoordinator<T extends TouchPoint> {

    ActionGraph run(T condition, ActionGraph actionGraph);

}
