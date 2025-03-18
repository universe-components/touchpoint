package com.universe.touchpoint.context;

import com.universe.touchpoint.monitor.metric.TaskMetric;
import com.universe.touchpoint.plan.ActionGraph;

public class TaskContext {

  private String goal;
  private ActionGraph graph;
  private TaskMetric metric;

  public TaskContext() {}

  public TaskContext(String goal) {
    this.goal = goal;
  }

  public String getGoal() {
    return goal;
  }

  public void setGoal(String goal) {
    this.goal = goal;
  }

  public ActionGraph getGraph() {
    return graph;
  }

  public void setGraph(ActionGraph graph) {
    this.graph = graph;
  }

  public TaskMetric getMetric() {
    return metric;
  }

  public void setMetric(TaskMetric metric) {
    this.metric = metric;
  }
}
