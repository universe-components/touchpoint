package com.universe.touchpoint.transport.actor;

import akka.actor.typed.ActorSystem;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.ActorConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

public class TouchPointActorRegistry<M extends TouchPoint>
    implements TouchPointTransportRegistry<ActorConfig> {

  private ActorSystem<M> system;

  @Override
  public void init(ActorConfig transportConfig) {}

  @Override
  public void register(
      AgentActionMeta agentActionMeta, String previousAction, String task, boolean isRequested) {
    String filter = TouchPointHelper.touchPointFilterName(previousAction);
    Class<M> messageType =
        isRequested ? (Class<M>) AgentAction.class : (Class<M>) AgentFinish.class;
    system = ActorSystem.create(PoolActor.create(messageType), filter);
  }

  public ActorSystem<M> getSystem() {
    return system;
  }
}
