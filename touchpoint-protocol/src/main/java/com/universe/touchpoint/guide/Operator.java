package com.universe.touchpoint.guide;

import com.universe.touchpoint.agent.AgentAction;

public interface Operator {

  <I, O> void run(AgentAction<I, O> action);
}
