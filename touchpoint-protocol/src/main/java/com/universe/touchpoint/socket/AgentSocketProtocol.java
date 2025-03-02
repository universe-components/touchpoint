package com.universe.touchpoint.socket;

import android.content.Context;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import javax.annotation.Nullable;

public interface AgentSocketProtocol {

    void initialize(AgentSocketConfig socketConfig);

    void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, Context context, String filterSuffix);

    <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context, ActionRole role);

}
