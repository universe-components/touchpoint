package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;

public class Supervisor extends RoleWorker {

  @Override
  public <I, O> void execute(AgentAction<I, O> agentAction) {
    RoleExecutor<O, ?> supervisor =
        (RoleExecutor<O, ?>)
            TaskRoleExecutor.getInstance(agentAction.getContext().getBelongTask())
                .getExecutor(agentAction.getInput().getActionBody().getAction());
    Object supervisedResult =
        supervisor.run(
            new SocketRequest<>(agentAction.getOutput().getBody()), agentAction.getContext());
    if (supervisedResult instanceof Boolean && !(Boolean) supervisedResult) {
      RoleWorker.run(agentAction);
    }
  }
}
