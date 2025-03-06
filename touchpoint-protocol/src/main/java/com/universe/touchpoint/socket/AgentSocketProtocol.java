package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import javax.annotation.Nullable;

public interface AgentSocketProtocol {

    void initialize(AgentSocketConfig socketConfig);

    void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, String filterSuffix);

    <C extends AgentContext> void registerReceiver(@Nullable C context, RoleType role);

}
