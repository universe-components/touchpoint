package com.universe.touchpoint.rolemodel.coordinator.action;

import com.universe.touchpoint.annotations.role.Coordinator;
import com.universe.touchpoint.annotations.task.TouchPointAction;
import com.universe.touchpoint.api.ActionBody;
import com.universe.touchpoint.api.SocketRequest;
import com.universe.touchpoint.api.operator.defaults.DefaultActionOperator;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;

@TouchPointAction(name = "switch_ai_model", desc = "switch ai model for action")
@Coordinator(task = "switch_ai_model")
public class SwitchAIModel4Action implements DefaultActionOperator {

  @Override
  public AgentActionMeta run(SocketRequest<ActionBody> operateMethod, TouchPointContext context) {
    return ((MetaRegion) TouchPointMemory.getRegion(Region.META))
        .getTouchPointAction(operateMethod.getBody().getTarget());
  }
}
