package com.universe.touchpoint.plan.executor;

import com.universe.touchpoint.Socket;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.memory.ActionSelector;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionExecutor;
import java.util.List;

public class AgentFinishExecutor<O> extends ActionExecutor<AgentFinish<O>, O> {

  @Override
  public void beforeRun(AgentFinish<O> touchPoint) {}

  @Override
  public O run(AgentFinish<O> agentFinish) {
    List<AgentActionMeta> predecessors = ActionSelector.nextAction(agentFinish, false);
    if (predecessors == null) {
      Socket.TaskCallbackListener callbackListener = agentFinish.getHeader().getCallbackListener();
      callbackListener.onSuccess(agentFinish);
    }
    return agentFinish.getOutput();
  }

  @Override
  public AgentFinish<O> afterRun(AgentFinish<O> agentFinish, O runResult) {
    return agentFinish;
  }
}
