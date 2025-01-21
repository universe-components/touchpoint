package com.universe.touchpoint;

import android.content.Context;

public interface TouchPointListener<T extends TouchPoint> {

    void onReceive(T touchPoint, Context context);
    String onAction(T action, Context context);

}
