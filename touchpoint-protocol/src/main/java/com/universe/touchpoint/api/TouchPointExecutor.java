package com.universe.touchpoint.api;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;

public interface TouchPointExecutor<I extends TouchPoint, O extends TouchPoint> {

    O run(I touchPoint, Context context);

}
