package com.universe.touchpoint.rolemodel.coordinator.action;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.Coordinator;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.operator.ActionOperator;
import com.universe.touchpoint.context.TouchPointContext;

@TouchPointAction(name = "switch_ai_model", desc = "switch ai model for action")
@Coordinator(task = "switch_ai_model")
public class SwitchAIModel4Action implements ActionOperator<ActionModel> {

  @Override
  public AgentAction<?, ?> run(ActionModel actionModel, TouchPointContext context) {
    context.getActionContext().addLangModel(actionModel.getAction(), actionModel.getModel());

    return null;
  }
}
