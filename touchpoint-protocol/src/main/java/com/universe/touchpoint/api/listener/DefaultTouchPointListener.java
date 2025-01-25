package com.universe.touchpoint.api.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointListener;

public interface DefaultTouchPointListener<T extends TouchPoint> extends TouchPointListener<T, Boolean> {

    @Override
    Boolean onReceive(T touchPoint, Context context);

}
