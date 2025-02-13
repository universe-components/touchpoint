package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessor;

public class DefaultResultProcessor<T extends TouchPoint> extends ResultProcessor<T, T> {

    public DefaultResultProcessor(T touchPoint,
                                  String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transportType) {
        super(touchPoint, goal, task, tpReceiver, context, transportType);
    }

    @Override
    public String process() {
        return null;
    }

}
