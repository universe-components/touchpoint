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
        .getContext()
        .getBelongTask()
        .equals(action.getContext().getBelongTask())) {
      new AgentSocketStateRouter<>()
          .route(
              new TaskContext(action.getInput().getContext().getBelongTask()),
              new AgentSocketStateMachine.AgentSocketStateContext<>(
                  AgentSocketState.ACTION_GRAPH_READY, action.getMeta()),
              action.getInput().getContext().getBelongTask());
    }
  }
}
