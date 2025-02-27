package com.universe.touchpoint.monitor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;

import java.util.Map;

public abstract class MetricSyncer {

    protected String task;

    public MetricSyncer(String task) {
        this.task = task;
    }

    public abstract void initialize(MetricSocketConfig config);

    public abstract void registerListener(Context context);

    public abstract void sendMetrics(Pair<TaskMetric, Map<String, ActionMetric>> metricPair, Context context);

}
