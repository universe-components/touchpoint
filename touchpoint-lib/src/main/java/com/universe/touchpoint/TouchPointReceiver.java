package com.universe.touchpoint;

import android.content.Context;

public interface TouchPointReceiver<T extends TouchPoint> {

    void onReceive(T touchPoint, Context context);

}
