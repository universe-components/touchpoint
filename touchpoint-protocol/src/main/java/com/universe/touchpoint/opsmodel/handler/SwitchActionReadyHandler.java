package com.universe.touchpoint.opsmodel.handler;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import com.universe.touchpoint.opsmodel.TaskRoleExecutor;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint>
    implements AgentSocketStateHandler<SocketRequest<I>, AgentActionMeta> {

  @Override
  public <C extends AgentContext> AgentActionMeta onStateChange(
      SocketRequest<I> request, C agentContext, String task) {
    TouchPointContext context = (TouchPointContext) agentContext;
    RoleExecutor<I, SocketResponse<AgentActionMeta, ?>> actionCoordinator =
        (RoleExecutor<I, SocketResponse<AgentActionMeta, ?>>)
            TaskRoleExecutor.getInstance(task).getExecutor(request.getActionBody().getAction());
    SocketResponse<AgentActionMeta, ?> newActionResponse =
        actionCoordinator.run(request, context).getBody();
    MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
    metaRegion.putTouchPointAction(
        newActionResponse.getBody().getName(), newActionResponse.getBody());
    return newActionResponse.getBody();
  }
}
