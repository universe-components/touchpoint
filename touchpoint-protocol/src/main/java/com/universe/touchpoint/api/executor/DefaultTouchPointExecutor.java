package com.universe.touchpoint.api.executor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointExecutor;

public interface DefaultTouchPointExecutor<T extends TouchPoint> extends TouchPointExecutor<T, TouchPoint> {

    @Override
    TouchPoint run(T touchPoint, Context context);

}
