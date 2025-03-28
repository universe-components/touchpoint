package com.universe.touchpoint.guide.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.guide.Operator;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;

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
