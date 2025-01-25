package com.universe.touchpoint.api;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointAction;

public interface DubboProvider<T extends TouchPoint, R> extends TouchPointAction {

    R invoke(T touchPoint);

}
