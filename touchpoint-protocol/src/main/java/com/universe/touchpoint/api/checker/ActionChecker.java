package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContext;

public interface ActionChecker<I extends TouchPoint, O> {

    O run(I touchPoint, TouchPointContext context);

}
