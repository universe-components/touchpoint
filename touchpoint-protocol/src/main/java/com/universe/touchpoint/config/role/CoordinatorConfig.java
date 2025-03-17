package com.universe.touchpoint.config.role;

import com.universe.touchpoint.annotations.role.OperateType;

public class CoordinatorConfig {

  private String task;
  private String scope;
  private OperateType operateType;

  public CoordinatorConfig(String task, String scope, OperateType operateType) {
    this.task = task;
    this.scope = scope;
    this.operateType = operateType;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public String getTask() {
    return task;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public OperateType getOperateType() {
    return operateType;
  }

  public void setOperateType(OperateType operateType) {
    this.operateType = operateType;
  }
}
