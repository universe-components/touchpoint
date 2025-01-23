package com.universe.touchpoint.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointListener;

public interface DefaultTouchPointListener<T extends TouchPoint> extends TouchPointListener<T, Boolean> {

    @Override
    Boolean onReceive(T touchPoint, Context context);

}
