package com.universe.touchpoint;

import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessorAdapter;

public class Dispatcher {

    public static String dispatch(String content, String task) {
        return ResultProcessorAdapter
                .getProcessor(null, content, task, null, null, Transport.BROADCAST).process();

    }

}