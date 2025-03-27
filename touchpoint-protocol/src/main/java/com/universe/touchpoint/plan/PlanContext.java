package com.universe.touchpoint.plan;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.context.AgentContext;

public class PlanContext<P> extends AgentContext {

  private final SocketRequest<P> params;
  private final Socket.TaskCallbackListener callbackListener;

  public PlanContext(
      SocketRequest<P> params, Socket.TaskCallbackListener callbackListener, String taskName) {
    super(taskName);
    this.params = params;
    this.callbackListener = callbackListener;
  }

  public SocketRequest<P> getParams() {
    return params;
  }

  public Socket.TaskCallbackListener getCallbackListener() {
    return callbackListener;
  }
}
