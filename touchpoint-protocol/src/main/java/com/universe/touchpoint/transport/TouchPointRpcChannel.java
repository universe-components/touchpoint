package com.universe.touchpoint.transport;

public abstract class TouchPointRpcChannel<I, O, C> implements TouchPointChannel<I, O> {

  protected final C transportConfig;

  public TouchPointRpcChannel(C transportConfig) {
    this.transportConfig = transportConfig;
  }
}
