package com.universe.touchpoint.plan.executor;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;

public class AgentActionExecutor<I, O> extends ActionExecutor<AgentAction<I, O>, O> {

  @Override
  public void beforeRun(AgentAction<I, O> action) {
    if (action.getMeta().getRole() == ActionRole.COORDINATOR) {
      ((AgentSyncProtocol<AgentAction<I, O>>)
              AgentSyncProtocolSelector.selectProtocol(SocketProtocol.MQTT5))
          .send(
              action,
              TouchPointHelper.touchPointFilterName(
                  TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER,
                  action.getContext().getBelongTask(),
                  RoleType.MEMBER.name()));
    }
  }

  @Override
  public O run(AgentAction<I, O> action) {
    String taskName = action.getContext().getBelongTask();
    RoleExecutor<I, O> tpReceiver =
        (RoleExecutor<I, O>)
            TaskRoleExecutor.getInstance(taskName).getExecutor(action.getActionName());
    return tpReceiver.run(action.getInput(), action.getContext());
  }

  @Override
  public AgentAction<I, O> afterRun(AgentAction<I, O> action, O runResult) {
    action.setOutput(runResult);
    if (action.getMeta().getRole() == ActionRole.SUPERVISOR) {
      SupervisorFactory.getSupervisor(action.getContext().getBelongTask())
          .execute(action, action.getContext().getBelongTask());
    }
    new Socket("collect_metrics").send(new SocketRequest<>(action));
    return action;
  }
}
