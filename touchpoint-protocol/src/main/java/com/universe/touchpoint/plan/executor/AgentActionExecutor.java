package com.universe.touchpoint.plan.executor;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.RoleWorker;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;

public class AgentActionExecutor<I, O> extends ActionExecutor<AgentAction<I, O>, O> {

  @Override
  public void beforeRun(AgentAction<I, O> action) {
    RoleWorker.run(action);
  }

  @Override
  public O run(AgentAction<I, O> action) {
    String taskName = action.getContext().getBelongTask();
    RoleExecutor<I, O> tpReceiver =
        (RoleExecutor<I, O>)
            TaskRoleExecutor.getInstance(taskName).getExecutor(action.getActionName());
    O runResult = tpReceiver.run(action.getInput(), action.getContext());
    action.setOutput(runResult);
    return runResult;
  }

  @Override
  public AgentAction<I, O> afterRun(AgentAction<I, O> action, O runResult) {
    RoleWorker.run(action);
    new Socket("collect_metrics").send(new SocketRequest<>(action));
    return action;
  }
}
