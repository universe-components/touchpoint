package com.universe.touchpoint.monitor.metric;

import java.util.concurrent.atomic.AtomicInteger;

public class ActionMetric {

    private final AtomicInteger predictionCount = new AtomicInteger(0);

    public int getPredictionCount() {
        return predictionCount.get();
    }

    public void incrementPredictionCount() {
        predictionCount.incrementAndGet();
    }

    public void addPredictionCount(int predictionCount) {
        this.predictionCount.addAndGet(predictionCount);
    }

    public void setPredictionCount(int predictionCount) {
        this.predictionCount.set(predictionCount);
    }

}
