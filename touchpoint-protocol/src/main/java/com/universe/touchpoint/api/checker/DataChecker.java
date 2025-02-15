package com.universe.touchpoint.api.checker;

import com.universe.touchpoint.TouchPoint;

public interface DataChecker<T extends TouchPoint> {

    boolean run(T data);

}
