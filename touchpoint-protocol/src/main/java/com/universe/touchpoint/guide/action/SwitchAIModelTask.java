package com.universe.touchpoint.guide.action;

import com.universe.touchpoint.annotations.task.OperateType;
import com.universe.touchpoint.annotations.task.TouchPointAction;

@TouchPointAction(
    name = "switch_ai_model",
    desc = "I want to switch ai model.",
    operateType = OperateType.PROPOSE_TASK)
public class SwitchAIModelTask {}
