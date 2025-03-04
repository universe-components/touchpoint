package com.universe.touchpoint.monitor;

import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;

import org.apache.commons.lang3.tuple.Pair;
import java.util.Map;

public interface MetricSyncer {

    void initialize(MetricSocketConfig config);

    void registerListener(String task);

    void sendMetrics(Pair<TaskMetric, Map<String, ActionMetric>> metricPair, String task);

}
