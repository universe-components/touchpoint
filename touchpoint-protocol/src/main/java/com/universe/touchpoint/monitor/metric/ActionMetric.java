package com.universe.touchpoint.monitor.metric;

import java.util.concurrent.atomic.AtomicInteger;

public class ActionMetric {

    private final AtomicInteger predictionCount = new AtomicInteger(0);

    public AtomicInteger getPredictionCount() {
        return predictionCount;
    }

}
