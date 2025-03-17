package com.universe.touchpoint.config.role;

public class SupervisorConfig extends RoleConfig {

  private String task;

  public SupervisorConfig(String task, String scopeAction) {
    super(scopeAction);
    this.task = task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getTask() {
    return task;
  }
}
