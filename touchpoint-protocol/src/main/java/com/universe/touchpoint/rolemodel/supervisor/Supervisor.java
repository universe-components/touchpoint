package com.universe.touchpoint.rolemodel.supervisor;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class Supervisor {

  public <I, O> void execute(AgentAction<I, O> agentAction, String task) {
    RoleExecutor<I, O> supervisor =
        (RoleExecutor<I, O>)
            TaskRoleExecutor.getInstance(task)
                .getExecutor(agentAction.getInput().getOperateMethod().getAction());
    Object supervisedResult = supervisor.run(agentAction.getInput(), agentAction.getContext());
    if (supervisedResult instanceof Boolean && !(Boolean) supervisedResult) {
      throw new RuntimeException(
          String.format(
              "ActionSupervisor run failed：action[%s] is not passed", agentAction.getActionName()));
    }
  }
}
