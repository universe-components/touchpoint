package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.OperateType;
import com.universe.touchpoint.config.role.CoordinatorConfig;

public class Coordinator extends RoleWorker {

  @Override
  public <I, O> void execute(AgentAction<I, O> action) {
    if (action.getInput().getActionBody() == null) {
      return;
    }
    OperateType operateType =
        ((CoordinatorConfig) action.getMeta().getRoleModel().getConfig()).getOperateType();
    operatorMap.get(operateType.name()).run(action);
  }
}
