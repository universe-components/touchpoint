package com.universe.touchpoint.transport;

public abstract class TouchPointRpcChannel<I, C> implements TouchPointChannel<I, String> {

  protected final C transportConfig;

  public TouchPointRpcChannel(C transportConfig) {
    this.transportConfig = transportConfig;
  }
}
