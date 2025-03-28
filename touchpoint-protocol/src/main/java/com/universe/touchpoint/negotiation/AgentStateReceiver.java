package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.security.TokenizerSelector;
import com.universe.touchpoint.sync.AgentReceiver;

public class AgentStateReceiver
    extends AgentReceiver<AgentSocketStateMachine.AgentSocketStateContext<?>> {

  @Override
  public <C extends AgentContext> void handleMessage(
      C context, AgentSocketStateMachine.AgentSocketStateContext<?> message, String topic) {
    boolean rs = new AgentSocketStateRouter<>().route(context, message, topic);
    if (rs) {
      TouchPointContext tpCtx = new TouchPointContext(context.getBelongTask());
      AgentActionMeta actionMeta =
          ((MetaRegion) TouchPointMemory.getRegion(Region.META))
              .getTouchPointAction(context.getBelongTask());
      ActionGraph actionGraph = ActionGraphBuilder.getTaskGraph(context.getBelongTask());
      tpCtx.getTaskContext().setGraph(actionGraph);
      tpCtx.getTaskContext().setGoal(actionMeta.getDesc());

      String token = TokenizerSelector.getTokenizer("jwt").generateToken(tpCtx);
      AgentSocketStateMachine.getInstance(context.getBelongTask())
          .getCallbackListener()
          .onResponse(token);
    }
  }
}
