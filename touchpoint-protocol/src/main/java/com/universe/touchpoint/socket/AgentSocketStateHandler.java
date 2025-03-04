package com.universe.touchpoint.socket;

public interface AgentSocketStateHandler<I, O> {

    <C extends AgentContext> O onStateChange(I input, C agentContext, String filterSuffix);

}
