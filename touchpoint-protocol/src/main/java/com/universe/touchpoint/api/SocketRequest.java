package com.universe.touchpoint.api;

public class SocketRequest<B> {

  private B body;
  private ActionBody<?> actionBody = new ActionBody<>();

  public SocketRequest(B body) {
    this.body = body;
  }

  public SocketRequest(ActionBody<?> actionBody) {
    this.actionBody = actionBody;
  }

  public SocketRequest(B body, ActionBody<?> actionBody) {
    this.body = body;
    this.actionBody = actionBody;
  }

  public B getBody() {
    return body;
  }

  public ActionBody<?> getActionBody() {
    return actionBody;
  }
}
