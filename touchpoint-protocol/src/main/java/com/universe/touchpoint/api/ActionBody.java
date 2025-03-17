package com.universe.touchpoint.api;

public class ActionBody<T> {

  private T target;
  private String action;

  public ActionBody() {}

  public T getTarget() {
    return target;
  }

  public void setTarget(T target) {
    this.target = target;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
