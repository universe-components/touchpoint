package com.universe.touchpoint.rolemodel.coordinator.handler;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.operator.ActionGraphOperator;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class ReorderActionReadyHandler<I extends TouchPoint, O extends TouchPoint>
    implements AgentSocketStateHandler<SocketRequest<I>, ActionGraph> {

  @Override
  public <C extends AgentContext> ActionGraph onStateChange(
      SocketRequest<I> request, C touchPointContext, String task) {
    TouchPointContext context = (TouchPointContext) touchPointContext;
    String coordinator = request.getOperateMethod().getAction();
    ActionGraphOperator<I> actionCoordinator =
        (ActionGraphOperator<I>) TaskRoleExecutor.getInstance(task).getExecutor(coordinator);
    ActionGraph graph = actionCoordinator.run(request, context);
    ActionGraphBuilder.putGraph(task, graph);
    return graph;
  }
}
