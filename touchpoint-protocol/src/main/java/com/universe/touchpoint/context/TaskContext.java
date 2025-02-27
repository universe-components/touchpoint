package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskContext {

    private final String goal;
    private String actionGraphName;
    private TaskMetric metric;

    public TaskContext(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public String getActionGraphName() {
        return actionGraphName;
    }

    public void setActionGraphName(String actionGraphName) {
        this.actionGraphName = actionGraphName;
    }

    public TaskMetric getMetric() {
        return metric;
    }

    public void setMetric(TaskMetric metric) {
        this.metric = metric;
    }

}
