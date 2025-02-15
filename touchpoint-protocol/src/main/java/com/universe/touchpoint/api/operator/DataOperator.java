package com.universe.touchpoint.api.operator;

import com.universe.touchpoint.TouchPoint;

public interface DataOperator<T extends TouchPoint> {

    T run(T data);

}
