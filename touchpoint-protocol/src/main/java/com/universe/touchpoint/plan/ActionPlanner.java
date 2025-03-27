package com.universe.touchpoint.plan;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.plan.selector.DefaultActionSelector;
import com.universe.touchpoint.plan.selector.RelevanceActionSelector;

public class ActionPlanner {

  public static <P> ActionSelector<P> getSelector(
      SocketRequest<P> params,
      Socket.TaskCallbackListener callbackListener,
      String task,
      boolean isUseModel) {
    if (isUseModel) {
      return new RelevanceActionSelector<>(params, callbackListener, task);
    }
    return new DefaultActionSelector<>();
  }
}
