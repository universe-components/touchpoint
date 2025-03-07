package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskContext {

    private String goal;
    private ActionGraphContext actionGraphContext;
    private TaskMetric metric;

    public TaskContext() {
    }

    public TaskContext(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public ActionGraphContext getActionGraphContext() {
        return actionGraphContext;
    }

    public void setActionGraphContext(ActionGraphContext actionGraphContext) {
        this.actionGraphContext = actionGraphContext;
    }

    public TaskMetric getMetric() {
        return metric;
    }

    public void setMetric(TaskMetric metric) {
        this.metric = metric;
    }

}
