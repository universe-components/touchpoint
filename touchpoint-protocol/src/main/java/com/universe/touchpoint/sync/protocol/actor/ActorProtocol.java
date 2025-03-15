package com.universe.touchpoint.sync.protocol.actor;

import akka.actor.typed.ActorSystem;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import javax.annotation.Nullable;

public class ActorProtocol<M> implements AgentSyncProtocol<M> {

  private ActorSystem<M> system;

  @Override
  public void initialize(AgentSocketConfig socketConfig) {}

  @Override
  public void send(M message, String filterSuffix) {
    system.tell(message);
  }

  @Override
  public <C extends AgentContext> void registerReceiver(
      @Nullable C context, String filter, RoleType role, Class<M> messageType) {
    assert context != null;
    String socketFilter =
        TouchPointHelper.touchPointFilterName(filter, context.getBelongTask(), role.name());
    system =
        ActorSystem.create(
            PoolActor.create(context, filter, socketFilter, messageType), socketFilter);
  }
}
