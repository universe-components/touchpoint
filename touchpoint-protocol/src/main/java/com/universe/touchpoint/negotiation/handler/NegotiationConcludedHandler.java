package com.universe.touchpoint.negotiation.handler;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.meta.MetaRegion;
import com.universe.touchpoint.meta.Region;
import com.universe.touchpoint.meta.TouchPointMemory;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NegotiationConcludedHandler implements AgentSocketStateHandler<Boolean, Boolean> {

  private static final Logger logger = LoggerFactory.getLogger(NegotiationConcludedHandler.class);

  @Override
  public <C extends AgentContext> Boolean onStateChange(
      Boolean ready, C agentContext, String filterSuffix) {
    if (ready) {
      MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
      metaRegion.clearTouchPointSwapActions();
      logger.info("Negotiation Concluded", "Collaborative relationship established");
      return true;
    }
    return false;
  }
}
