package com.universe.touchpoint.layer;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.sync.AgentReceiver;

public class AgentContextReceiver extends AgentReceiver<AgentAction<?, ?>> {

  @Override
  public <C extends AgentContext> void handleMessage(
      C context, AgentAction<?, ?> action, String topic) {
    RoleWorker.run(action);
  }
}
