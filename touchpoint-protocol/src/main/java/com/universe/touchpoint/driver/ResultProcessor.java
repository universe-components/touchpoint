package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.Transport;

public abstract class ResultProcessor<R, T extends TouchPoint> {

    protected final R result;
    protected final String goal;
    protected final String task;
    protected final TouchPointListener<T, ?>  tpReceiver;
    protected final Context context;
    protected final Transport transportType;

    public ResultProcessor(R result, String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transport) {
        this.result = result;
        this.goal = goal;
        this.task = task;
        this.tpReceiver = tpReceiver;
        this.context = context;
        this.transportType = transport;
    }

    public abstract String process();

}
