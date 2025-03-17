package com.universe.touchpoint.rolemodel.coordinator.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import com.universe.touchpoint.rolemodel.coordinator.Operator;

public class EditActionOperator implements Operator {

  @Override
  public void run(AgentAction<?, ?> action) {
    new AgentSocketStateRouter<>()
        .route(
            action.getContext(),
            new AgentSocketStateMachine.AgentSocketStateContext<>(
                AgentSocketState.ACTION_READY, action.getInput()),
            action.getContext().getBelongTask());
  }
}
