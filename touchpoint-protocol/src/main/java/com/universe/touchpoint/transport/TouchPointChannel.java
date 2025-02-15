package com.universe.touchpoint.transport;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;

public abstract class TouchPointChannel<R> {

    protected final Context context;

    public TouchPointChannel(Context context) {
        this.context = context;
    }

    public abstract <I extends TouchPoint, O extends TouchPoint> R send(I touchpoint);

}
