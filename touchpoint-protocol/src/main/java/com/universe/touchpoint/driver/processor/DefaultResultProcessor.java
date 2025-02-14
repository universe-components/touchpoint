package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessor;

public class DefaultResultProcessor<T extends TouchPoint> extends ResultProcessor<T, T> {

    public DefaultResultProcessor(T touchPoint,
                                  String goal, String task, Context context, Transport transportType) {
        super(touchPoint, goal, task, context, transportType);
    }

    @Override
    public String process() {
        return null;
    }

}
