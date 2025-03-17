package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.agent.AgentAction;

public interface Operator {

  void run(AgentAction<?, ?> action);
}
