package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.ActionMetric;

import java.util.Map;

public class ActionContext {

    private Map<String, ActionMetric> actionMetrics;

    public ActionMetric getActionMetric(String action) {
        return actionMetrics.computeIfAbsent(action, k -> new ActionMetric());
    }

}
