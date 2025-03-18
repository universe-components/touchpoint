package com.universe.touchpoint.api;

import com.universe.touchpoint.context.TouchPointContext;

public class SocketRequest<B> {

  private B body;
  private ActionBody<?> actionBody = new ActionBody<>();
  private TouchPointContext context;
  private String token;

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

  public TouchPointContext getContext() {
    return context;
  }

  public void setContext(TouchPointContext context) {
    this.context = context;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
