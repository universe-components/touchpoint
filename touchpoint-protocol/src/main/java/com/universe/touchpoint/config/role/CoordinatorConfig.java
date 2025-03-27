package com.universe.touchpoint.config.role;

import com.universe.touchpoint.annotations.task.OperateType;

public class CoordinatorConfig extends RoleConfig {

  private String task;
  private OperateType operateType;

  public CoordinatorConfig(String task, String scope, OperateType operateType) {
    super(scope);
    this.task = task;
    this.operateType = operateType;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getTask() {
    return task;
  }

  public OperateType getOperateType() {
    return operateType;
  }

  public void setOperateType(OperateType operateType) {
    this.operateType = operateType;
  }
}
