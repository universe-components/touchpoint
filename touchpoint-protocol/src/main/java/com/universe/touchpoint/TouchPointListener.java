package com.universe.touchpoint;

import android.content.Context;

public interface TouchPointListener<T extends TouchPoint, R> {

    R onReceive(T touchPoint, Context context);

}
