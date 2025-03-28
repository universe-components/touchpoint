package com.universe.touchpoint.guide.handler;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.operator.ActionGraphOperator;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.guide.TaskRoleExecutor;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;

public class ReorderActionReadyHandler<I extends TouchPoint, O extends TouchPoint>
    implements AgentSocketStateHandler<SocketRequest<I>, ActionGraph> {

  @Override
  public <C extends AgentContext> ActionGraph onStateChange(
      SocketRequest<I> request, C touchPointContext, String task) {
    TouchPointContext context = (TouchPointContext) touchPointContext;
    String coordinator = request.getActionBody().getAction();
    ActionGraphOperator<I> actionCoordinator =
        (ActionGraphOperator<I>) TaskRoleExecutor.getInstance(task).getExecutor(coordinator);
    ActionGraph graph = actionCoordinator.run(request, context).getBody();
    ActionGraphBuilder.putGraph(task, graph);
    return graph;
  }
}
