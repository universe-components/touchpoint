package com.universe.touchpoint.driver.processor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultProcessor;

public class DefaultResultProcessor<T extends TouchPoint> implements ResultProcessor<T> {

    @Override
    public String process(T touchPoint,
                          String goal, String task, Context context, Transport transportType) {
        return null;
    }

}
