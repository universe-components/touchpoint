package com.universe.touchpoint.guide.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.guide.Operator;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import com.universe.touchpoint.negotiation.context.TaskContext;

public class SwitchTaskOperator implements Operator {

  @Override
  public <I, O> void run(AgentAction<I, O> action) {
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
