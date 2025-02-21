package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.TaskMetric;

public class TaskContext {

    private String taskName;
    private final String goal;
    private final TaskMetric metric = new TaskMetric();

    public TaskContext(String goal) {
        this.goal = goal;
    }

    public TaskContext(String goal, String taskName) {
        this.goal = goal;
        this.taskName = taskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getGoal() {
        return goal;
    }

    public TaskMetric getMetric() {
        return metric;
    }

}
