package com.universe.touchpoint.config.role;

public class SupervisorConfig {

  private String task;
  private String scopeAction;

  public SupervisorConfig(String task, String scopeAction) {
    this.task = task;
    this.scopeAction = scopeAction;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getTask() {
    return task;
  }

  public String getScopeAction() {
    return scopeAction;
  }

  public void setScopeAction(String scopeAction) {
    this.scopeAction = scopeAction;
  }
}
