package com.universe.touchpoint.api;

import com.universe.touchpoint.exception.TouchPointException;

public class SocketResponse<B, P> {

  private final B body;
  private TouchPointException<P> exception;

  public SocketResponse(B body) {
    this.body = body;
  }

  public SocketResponse(B body, TouchPointException<P> exception) {
    this.body = body;
    this.exception = exception;
  }

  public B getBody() {
    return body;
  }

  public TouchPointException<P> getException() {
    return exception;
  }

  public void setException(TouchPointException<P> exception) {
    this.exception = exception;
  }
}
