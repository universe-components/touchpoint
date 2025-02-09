package com.universe.touchpoint.api;

import com.universe.touchpoint.TouchPoint;

public interface Operator<O> {

    O run(TouchPoint input);

}
