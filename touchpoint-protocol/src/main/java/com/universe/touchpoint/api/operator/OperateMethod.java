package com.universe.touchpoint.api.operator;

public class OperateMethod {

  private String target;
  private String action;

  public OperateMethod() {}

  public OperateMethod(String target, String action) {
    this.target = target;
    this.action = action;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
