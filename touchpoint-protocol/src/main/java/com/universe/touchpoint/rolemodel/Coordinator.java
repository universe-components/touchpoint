package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.OperateType;
import com.universe.touchpoint.config.role.CoordinatorConfig;
import com.universe.touchpoint.rolemodel.coordinator.OperatorSelector;

public class Coordinator extends RoleWorker {

  @Override
  public <I, O> void execute(AgentAction<I, O> action) {
    if (action.getInput().getActionBody() == null) {
      return;
    }
    OperateType operateType =
        ((CoordinatorConfig) action.getMeta().getRoleModel().getConfig()).getOperateType();
    OperatorSelector.getOperator(operateType).run(action);
  }
}
