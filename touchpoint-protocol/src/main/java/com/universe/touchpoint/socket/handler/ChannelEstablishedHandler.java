package com.universe.touchpoint.socket.handler;

import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChannelEstablishedHandler implements AgentSocketStateHandler<Boolean, Boolean> {

    private static final Logger logger = LoggerFactory.getLogger(ChannelEstablishedHandler.class);

    @Override
    public <C extends AgentContext> Boolean onStateChange(Boolean ready, C agentContext, String filterSuffix) {
        if (ready) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            metaRegion.clearTouchPointSwapActions();
            logger.info("ChannelEstablishedHandler", "Collaborative relationship established");
            return true;
        }
        return false;
    }

}
