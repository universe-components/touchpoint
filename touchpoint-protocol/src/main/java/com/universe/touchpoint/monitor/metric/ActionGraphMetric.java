package com.universe.touchpoint.monitor.metric;

import java.util.concurrent.atomic.AtomicInteger;

public class ActionGraphMetric {

    private final AtomicInteger faultActionCount = new AtomicInteger(0);
    private final AtomicInteger retryActionCount = new AtomicInteger(0);

    public AtomicInteger getFaultActionCount() {
        return faultActionCount;
    }

    public AtomicInteger getRetryActionCount() {
        return retryActionCount;
    }

    public void addFaultActionCount() {
        faultActionCount.incrementAndGet();
    }

    public void addRetryActionCount(int retryCount) {
        retryActionCount.set(retryCount);
    }

}
