package com.universe.touchpoint.config.role;

public class ExceptionHandlerConfig extends RoleConfig {

  private String task;
  private Integer errorCode;

  public ExceptionHandlerConfig(String task, Integer errorCode, String scopeAction) {
    super(scopeAction);
    this.task = task;
    this.errorCode = errorCode;
  }

  public String getTask() {
    return task;
  }

  public void setTask(String task) {
    this.task = task;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }
}
