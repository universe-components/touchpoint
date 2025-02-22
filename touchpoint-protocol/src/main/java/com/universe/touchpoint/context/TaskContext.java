package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskContext {

    private final String goal;
    private final TaskMetric metric = new TaskMetric();

    public TaskContext(String goal) {
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public TaskMetric getMetric() {
        return metric;
    }

}
