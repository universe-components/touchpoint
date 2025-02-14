package com.universe.touchpoint.api;

import com.universe.touchpoint.TouchPoint;

public interface ActionSupervisor<T extends TouchPoint> {

    boolean run(T touchPoint);

}
