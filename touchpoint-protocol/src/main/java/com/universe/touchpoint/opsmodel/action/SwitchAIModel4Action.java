package com.universe.touchpoint.opsmodel.action;

import com.universe.touchpoint.annotations.task.OperateType;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.ActionBody;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.SocketResponse;
import com.universe.touchpoint.api.operator.defaults.DefaultActionOperator;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;

@TouchPointAction(
    name = "switch_ai_model",
    desc = "switch ai model for action",
    operateType = OperateType.EDIT_ACTION)
public class SwitchAIModel4Action implements DefaultActionOperator<String> {

  @Override
  public SocketResponse<AgentActionMeta, ?> run(
      SocketRequest<ActionBody<String>> operateMethod, TouchPointContext context) {
    AgentActionMeta actionMeta =
        ((MetaRegion) TouchPointMemory.getRegion(Region.META))
            .getTouchPointAction(operateMethod.getBody().getTarget());
    return new SocketResponse<>(actionMeta);
  }
}
