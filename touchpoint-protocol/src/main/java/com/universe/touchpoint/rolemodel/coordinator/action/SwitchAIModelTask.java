package com.universe.touchpoint.rolemodel.coordinator.action;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.task.TouchPointAction;

@TouchPointAction(
    name = "switch_ai_model",
    desc = "I want to switch ai model.",
    role = ActionRole.PROPOSER)
public class SwitchAIModelTask {}
