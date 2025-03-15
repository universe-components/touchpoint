package com.universe.touchpoint;

import com.universe.touchpoint.api.SocketRequest;

public class Socket {

  protected final String action;

  public Socket(String action) {
    this.action = action;
  }

  public <P, F> F send(SocketRequest<P> params) {
    return send(params, null);
  }

  public <P, F> F send(SocketRequest<P> params, TaskCallbackListener callbackListener) {
    return Dispatcher.dispatch(action, params, callbackListener);
  }

  public abstract static class TaskCallbackListener {

    public abstract <T> void onSuccess(T result);
  }
}
