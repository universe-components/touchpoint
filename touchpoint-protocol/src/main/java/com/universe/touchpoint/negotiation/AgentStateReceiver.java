package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TouchPointContext;
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
      TouchPoint touchPoint = new TouchPoint();
      ActionGraph actionGraph = ActionGraphBuilder.getTaskGraph(context.getBelongTask());
      TouchPointContext tpCtx = new TouchPointContext(context.getBelongTask());
      String token = TokenizerSelector.getTokenizer("jwt").generateToken(actionGraph);
      tpCtx.setToken(token);
      touchPoint.setContext(tpCtx);
      AgentSocketStateMachine.getInstance(context.getBelongTask())
          .getCallbackListener()
          .onResponse(touchPoint);
    }
  }
}
