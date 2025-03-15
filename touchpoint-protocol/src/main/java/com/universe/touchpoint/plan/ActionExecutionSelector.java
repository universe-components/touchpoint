package com.universe.touchpoint.plan;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.executor.AgentActionExecutor;
import com.universe.touchpoint.plan.executor.AgentFinishExecutor;

public class ActionExecutionSelector {

  public static ActionExecutor<?, ?> getExecutor(TouchPoint touchPoint) {
    if (touchPoint instanceof AgentFinish) {
      return new AgentFinishExecutor<>();
    }
    return new AgentActionExecutor<>();
  }
}
