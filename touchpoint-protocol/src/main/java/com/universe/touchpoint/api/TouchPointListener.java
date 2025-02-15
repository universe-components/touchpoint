package com.universe.touchpoint.api;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointAction;

public interface TouchPointListener<T extends TouchPoint, R extends TouchPoint> extends TouchPointAction {

    R onReceive(T touchPoint, Context context);

}
