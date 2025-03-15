package com.universe.touchpoint.api;

import com.universe.touchpoint.api.operator.OperateMethod;

public class SocketRequest<B> {

  private B body;
  private OperateMethod operateMethod;

  public SocketRequest(B body) {
    this.body = body;
  }

  public SocketRequest(OperateMethod operateMethod) {
    this.operateMethod = operateMethod;
  }

  public SocketRequest(B body, OperateMethod operateMethod) {
    this.body = body;
    this.operateMethod = operateMethod;
  }

  public B getBody() {
    return body;
  }

  public OperateMethod getOperateMethod() {
    return operateMethod;
  }
}
