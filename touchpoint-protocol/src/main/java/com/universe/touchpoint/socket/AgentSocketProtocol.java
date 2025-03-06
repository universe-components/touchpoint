package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import javax.annotation.Nullable;

public interface AgentSocketProtocol {

    void initialize(AgentSocketConfig socketConfig);

    <M> void send(M message, String filterSuffix);

    <C extends AgentContext> void registerReceiver(@Nullable C context, String filter, RoleType role);

}
