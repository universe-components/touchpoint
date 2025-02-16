package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.config.transport.Transport;

public interface ResultProcessor<R> {

    String process(R result, String goal, String task, Context context, Transport transport);

}
