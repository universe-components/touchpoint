package com.universe.touchpoint.sync;

import com.universe.touchpoint.negotiation.AgentContext;

public interface AgentReceiver<M> {

    <C extends AgentContext> void handleMessage(C context, M message, String topic);

}
