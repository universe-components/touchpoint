package com.universe.touchpoint.api;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;

public interface TouchPointListener<T extends TouchPoint, R extends TouchPoint> {

    R onReceive(T touchPoint, Context context);

}
