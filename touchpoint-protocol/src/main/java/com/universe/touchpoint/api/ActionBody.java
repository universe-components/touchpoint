package com.universe.touchpoint.api;

public class ActionBody {

  private String target;
  private String action;

  public ActionBody() {}

  public ActionBody(String target, String action) {
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
