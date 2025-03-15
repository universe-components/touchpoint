package com.universe.touchpoint.rolemodel.coordinator.handler;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint>
    implements AgentSocketStateHandler<SocketRequest<I>, AgentActionMeta> {

  @Override
  public <C extends AgentContext> AgentActionMeta onStateChange(
      SocketRequest<I> request, C agentContext, String task) {
    TouchPointContext context = (TouchPointContext) agentContext;
    RoleExecutor<I, AgentActionMeta> actionCoordinator =
        (RoleExecutor<I, AgentActionMeta>)
            TaskRoleExecutor.getInstance(task).getExecutor(request.getOperateMethod().getAction());
    AgentActionMeta newAction = actionCoordinator.run(request, context);
    MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
    metaRegion.putTouchPointAction(newAction.getName(), newAction);
    return newAction;
  }
}
