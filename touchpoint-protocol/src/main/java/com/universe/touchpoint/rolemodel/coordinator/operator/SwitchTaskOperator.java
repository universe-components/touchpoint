package com.universe.touchpoint.rolemodel.coordinator.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import com.universe.touchpoint.negotiation.context.TaskContext;
import com.universe.touchpoint.rolemodel.coordinator.Operator;

public class SwitchTaskOperator implements Operator {

  @Override
  public void run(AgentAction<?, ?> action) {
    if (!action
        .getInput()
        .getActionBody()
        .getTarget()
        .equals(action.getContext().getBelongTask())) {
      new AgentSocketStateRouter<>()
          .route(
              new TaskContext((String) action.getInput().getActionBody().getTarget()),
              new AgentSocketStateMachine.AgentSocketStateContext<>(
                  AgentSocketState.ACTION_GRAPH_READY, action.getMeta()),
              action.getContext().getBelongTask());
    }
  }
}
