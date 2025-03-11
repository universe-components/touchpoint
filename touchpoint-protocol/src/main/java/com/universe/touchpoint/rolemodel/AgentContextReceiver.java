package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.sync.AgentReceiver;

public class AgentContextReceiver extends AgentReceiver<TouchPointContext> {

    @Override
    public <C extends AgentContext> void handleMessage(C context, TouchPointContext ctx, String topic) {
        CoordinatorFactory.getCoordinator(context.getBelongTask()).execute(ctx);
    }

}
