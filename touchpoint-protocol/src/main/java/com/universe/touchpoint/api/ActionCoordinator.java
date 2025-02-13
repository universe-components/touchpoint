package com.universe.touchpoint.api;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.driver.ActionGraph;

public interface ActionCoordinator {

    ActionGraph run(TouchPoint condition, ActionGraph actionGraph);

}
