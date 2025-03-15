package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.context.AgentContext;

public interface AgentSocketStateHandler<I, O> {

  <C extends AgentContext> O onStateChange(I input, C agentContext, String filterSuffix);
}
