package com.universe.touchpoint.sync;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.context.AgentContext;
import javax.annotation.Nullable;

public interface AgentSyncProtocol<M> {

  void initialize(AgentSocketConfig socketConfig);

  void send(M message, String filterSuffix);

  <C extends AgentContext> void registerReceiver(
      @Nullable C context, String filter, RoleType role, Class<M> messageType);
}
