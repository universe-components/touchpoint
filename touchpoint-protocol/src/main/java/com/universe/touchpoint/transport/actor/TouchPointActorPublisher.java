package com.universe.touchpoint.transport.actor;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

public class TouchPointActorPublisher<M extends TouchPoint>
    implements TouchPointChannel<M, Boolean> {

  @Override
  public Boolean send(M touchpoint) {
    TouchPointActorRegistry<M> actorRegistry =
        (TouchPointActorRegistry<M>)
            TouchPointTransportRegistryFactory.getRegistry(Transport.ACTOR);
    actorRegistry.getSystem().tell(touchpoint);
    return true;
  }
}
