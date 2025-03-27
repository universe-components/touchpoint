package com.universe.touchpoint.opsmodel.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import com.universe.touchpoint.opsmodel.Operator;

public class EditActionOperator implements Operator {

  @Override
  public <I, O> void run(AgentAction<I, O> action) {
    new AgentSocketStateRouter<>()
        .route(
            action.getContext(),
            new AgentSocketStateMachine.AgentSocketStateContext<>(
                AgentSocketState.ACTION_READY, action.getInput()),
            action.getContext().getBelongTask());
  }
}
