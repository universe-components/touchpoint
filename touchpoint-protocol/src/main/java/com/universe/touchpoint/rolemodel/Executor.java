package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.config.role.ExceptionHandlerConfig;
import com.universe.touchpoint.exception.TouchPointException;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import java.util.Objects;

public class Executor extends RoleWorker {

  @Override
  public <I, O> void execute(AgentAction<I, O> agentAction) {
    Integer errorCode = (Integer) agentAction.getInput().getActionBody().getTarget();
    String nextAction = agentAction.getInput().getActionBody().getAction();
    AgentActionMeta actionMeta =
        ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAction(nextAction);
    if (Objects.equals(
        ((ExceptionHandlerConfig) actionMeta.getRoleModel().getConfig()).getErrorCode(),
        errorCode)) {
      RoleExecutor<TouchPointException<?>, ?> executor =
          (RoleExecutor<TouchPointException<?>, ?>)
              TaskRoleExecutor.getInstance(agentAction.getContext().getBelongTask())
                  .getExecutor(agentAction.getInput().getActionBody().getAction());
      executor.run(
          new SocketRequest<>((TouchPointException<?>) agentAction.getOutput()),
          agentAction.getContext());
    }
  }
}
