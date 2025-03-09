package com.universe.touchpoint.negotiation.handler;

import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NegotiationConcludedHandler implements AgentSocketStateHandler<Boolean, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(NegotiationConcludedHandler.class);

    @Override
    public <C extends AgentContext> Boolean onStateChange(Boolean ready, C agentContext, String filterSuffix) {
        if (ready) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            metaRegion.clearTouchPointSwapActions();
            logger.info("Negotiation Concluded", "Collaborative relationship established");
            return true;
        }
        return false;
    }

}
