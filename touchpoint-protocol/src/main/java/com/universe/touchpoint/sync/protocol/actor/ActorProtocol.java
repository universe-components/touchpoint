package com.universe.touchpoint.sync.protocol.actor;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.sync.AgentSyncProtocol;

import javax.annotation.Nullable;
import akka.actor.typed.ActorSystem;

public class ActorProtocol implements AgentSyncProtocol {

    private ActorSystem<Object> system;

    @Override
    public void initialize(AgentSocketConfig socketConfig) {
    }

    @Override
    public <M> void send(M message, String filterSuffix) {
        system.tell(message);
    }

    @Override
    public <C extends AgentContext> void registerReceiver(@Nullable C context, String filter, RoleType role) {
        assert context != null;
        String socketFilter = TouchPointHelper.touchPointFilterName(filter, context.getBelongTask(), role.name());
        system = ActorSystem.create(PoolActor.create(context, filter, socketFilter), socketFilter);
    }

}
