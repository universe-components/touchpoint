package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import com.universe.touchpoint.security.TokenizerSelector;
import java.util.Objects;

public class Coordinator {

  public void execute(AgentAction<?, ?> action) {
    if (action.getInput().getActionBody() == null) {
      return;
    }
    TouchPointContext prevContext =
        (TouchPointContext)
            TokenizerSelector.getTokenizer("jwt").parseToken(action.getContext().getToken());
    String prevGraphName = prevContext.getActionGraph().getName();
    String currGraphName = action.getInput().getActionBody().getTarget();

    if (currGraphName != null && !Objects.equals(prevGraphName, currGraphName)) {
      new AgentSocketStateRouter<>()
          .route(
              action.getContext(),
              new AgentSocketStateMachine.AgentSocketStateContext<>(
                  AgentSocketState.COORDINATOR_ACTION_GRAPH_READY, action.getInput()),
              action.getContext().getBelongTask());
    } else {
      new AgentSocketStateRouter<>()
          .route(
              action.getContext(),
              new AgentSocketStateMachine.AgentSocketStateContext<>(
                  AgentSocketState.ACTION_READY, action.getInput()),
              action.getContext().getBelongTask());
    }
  }
}
