package com.universe.touchpoint.monitor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;

import java.util.Map;

public interface MetricSyncer {

    void initialize(MetricSocketConfig config);

    void registerListener(String task, Context context);

    void sendMetrics(Pair<TaskMetric, Map<String, ActionMetric>> metricPair, String task, Context context);

}
