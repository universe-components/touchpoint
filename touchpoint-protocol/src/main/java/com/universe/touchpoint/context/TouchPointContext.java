package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.ActionMetric;
import java.util.Map;

public class TouchPointContext {

    private TaskContext task;
    private String action;
    private Map<String, ActionMetric> actionMetrics;
    private Map<String, Object> extContext;

    public TaskContext getTask() {
        return task;
    }

    public void setTask(TaskContext task) {
        this.task = task;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ActionMetric getActionMetric(String action) {
        return actionMetrics.computeIfAbsent(action, k -> new ActionMetric());
    }

    public void addExtContext(String name, Object context) {
        extContext.put(name, context);
    }

    public Object getExtContext(String name) {
        return extContext.get(name);
    }

}
