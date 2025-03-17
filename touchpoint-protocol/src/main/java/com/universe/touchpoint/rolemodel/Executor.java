package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.exception.TouchPointException;

public class Executor extends RoleWorker {

  @Override
  public <I, O> void execute(AgentAction<I, O> agentAction) {
    RoleExecutor<TouchPointException<?>, ?> executor =
        (RoleExecutor<TouchPointException<?>, ?>)
            TaskRoleExecutor.getInstance(agentAction.getContext().getBelongTask())
                .getExecutor(agentAction.getInput().getActionBody().getAction());
    executor.run(
        new SocketRequest<>((TouchPointException<?>) agentAction.getOutput()),
        agentAction.getContext());
  }
}
