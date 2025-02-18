package com.universe.touchpoint.monitor.metric;

import java.util.concurrent.atomic.AtomicInteger;

public class ActionGraphMetric {

    private final AtomicInteger faultActionCount = new AtomicInteger(0);
    private final AtomicInteger retryActionCount = new AtomicInteger(0);

    public int getFaultActionCount() {
        return faultActionCount.get();
    }

    public int getRetryActionCount() {
        return retryActionCount.get();
    }

    public void addFaultActionCount() {
        faultActionCount.incrementAndGet();
    }

    public void setRetryActionCount(int retryCount) {
        retryActionCount.set(retryCount);
    }

    public void incrementRetryActionCount() {
        retryActionCount.incrementAndGet();
    }

    public void addRetryActionCount(int retryCount) {
        retryActionCount.addAndGet(retryCount);
    }

}
