package com.universe.touchpoint.guide.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.guide.Operator;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import java.util.Objects;

public class OrganizeActionOperator implements Operator {

  @Override
  public <I, O> void run(AgentAction<I, O> action) {
    TouchPointContext prevContext = action.getContext();
    String prevGraphName = prevContext.getActionGraph().getName();
    String currGraphName = (String) action.getInput().getActionBody().getTarget();
    if (currGraphName != null && !Objects.equals(prevGraphName, currGraphName)) {
      new AgentSocketStateRouter<>()
          .route(
              action.getContext(),
              new AgentSocketStateMachine.AgentSocketStateContext<>(
                  AgentSocketState.COORDINATOR_ACTION_GRAPH_READY, action.getInput()),
              action.getContext().getBelongTask());
    }
  }
}
