package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;

public interface ActionGraphChecker<I extends TouchPoint, O> {

    O run(I touchPoint, String task);

}
