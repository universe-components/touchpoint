package com.universe.touchpoint.opsmodel.operator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.opsmodel.Operator;
import com.universe.touchpoint.opsmodel.RoleWorker;
import com.universe.touchpoint.opsmodel.TaskRoleExecutor;

public class CheckDataOperator implements Operator {

  @Override
  public <I, O> void run(AgentAction<I, O> agentAction) {
    RoleExecutor<O, ?> supervisor =
        (RoleExecutor<O, ?>)
            TaskRoleExecutor.getInstance(agentAction.getContext().getBelongTask())
                .getExecutor(agentAction.getInput().getActionBody().getAction());
    Object checkResult =
        supervisor.run(
            new SocketRequest<>(agentAction.getOutput().getBody()), agentAction.getContext());
    if (checkResult instanceof Boolean && !(Boolean) checkResult) {
      RoleWorker.run(agentAction);
    }
  }
}
