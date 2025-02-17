package com.universe.touchpoint.monitor;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.monitor.metric.ActionGraphMetric;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskMetricManager {

    private static final Map<String, ActionGraphMetric> taskMetrics = new ConcurrentHashMap<>();

    public static ActionGraphMetric getTaskMetric(String task) {
        if (!taskMetrics.containsKey(task)) {
            taskMetrics.put(task, new ActionGraphMetric());
        }
        return taskMetrics.get(task);
    }

    public static void addTaskMetric(AgentAction<?, ?> action) {
        TaskMetricConfig taskMetricConfig = ConfigManager.selectTaskMetricConfig(action.getTask());
        assert taskMetricConfig != null;
        if (action.getMetric().getPredictionCount().get() > 0) {
            getTaskMetric(action.getTask()).addRetryActionCount(action.getMetric().getPredictionCount().get());
        }
    }

}
