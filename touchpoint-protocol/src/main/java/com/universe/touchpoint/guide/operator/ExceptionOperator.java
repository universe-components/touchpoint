package com.universe.touchpoint.guide.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.guide.Operator;
import com.universe.touchpoint.guide.TaskRoleExecutor;
import java.util.Objects;

public class ExceptionOperator implements Operator {

  @Override
  public <I, O> void run(AgentAction<I, O> agentAction) {
    Integer errorCode = agentAction.getOutput().getException().getErrorCode();
    String nextAction = agentAction.getInput().getActionBody().getAction();
    if (Objects.equals(Integer.getInteger(nextAction), errorCode)) {
      RoleExecutor<SocketResponse<O, ?>, ?> executor =
          (RoleExecutor<SocketResponse<O, ?>, ?>)
              TaskRoleExecutor.getInstance(agentAction.getContext().getBelongTask())
                  .getExecutor(agentAction.getInput().getActionBody().getAction());
      executor.run(new SocketRequest<>(agentAction.getOutput()), agentAction.getContext());
    }
  }
}
