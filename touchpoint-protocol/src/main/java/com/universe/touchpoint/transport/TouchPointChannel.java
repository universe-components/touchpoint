package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;

public abstract class TouchPointChannel {

    protected final Context context;

    public TouchPointChannel(Context context) {
        this.context = context;
    }

    public abstract <T extends TouchPoint> boolean send(T touchpoint);

}
