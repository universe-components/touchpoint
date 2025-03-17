package com.universe.touchpoint.exception;

public class TouchPointException<P> {

  private final int errorCode;
  private final P params;
  private final String message;

  public TouchPointException(int errorCode, P params, String message) {
    this.errorCode = errorCode;
    this.params = params;
    this.message = message;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public P getParams() {
    return params;
  }

  public String getMessage() {
    return message;
  }
}
