package com.universe.touchpoint.transport.local;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ActionExecutionSelector;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.plan.ResultExchanger;
import com.universe.touchpoint.transport.TouchPointChannel;

public class TouchPointLocalExecutor<I, O, T extends AgentAction<I, O>>
    implements TouchPointChannel<T, O> {

  @Override
  public O send(T action) {
    action = ((ActionExecutor<T, O>) ActionExecutionSelector.getExecutor(action)).execute(action);
    action.setOutput(action.getOutput());
    return new ResultExchanger()
        .exchange(
            action,
            action.getContext().getTaskContext().getGoal(),
            action.getContext().getTask(),
            Transport.LOCAL);
  }
}
