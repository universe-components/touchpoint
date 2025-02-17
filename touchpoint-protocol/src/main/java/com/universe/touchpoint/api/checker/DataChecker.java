package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;

public interface DataChecker<I extends TouchPoint, O> {

    O run(I data);

}
