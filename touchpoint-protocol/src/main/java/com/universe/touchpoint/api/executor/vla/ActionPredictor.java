package com.universe.touchpoint.api.executor.vla;

import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.api.executor.AgentActionExecutor;
import com.universe.touchpoint.context.TouchPointContext;

public abstract class ActionPredictor
    extends AgentActionExecutor<ImageData, OpenVLA.ActionRequest> {

  @Override
  public SocketResponse<OpenVLA.ActionRequest, ?> run(
      SocketRequest<ImageData> imageData, TouchPointContext context) {
    String goal = context.getTaskContext().getGoal();
    return new SocketResponse<>(new OpenVLA.ActionRequest(imageData.getBody().getData(), goal));
  }
}
