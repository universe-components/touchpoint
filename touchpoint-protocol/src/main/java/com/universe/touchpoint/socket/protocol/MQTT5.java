package com.universe.touchpoint.socket.protocol;

import android.content.Context;

import androidx.annotation.Nullable;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.AgentSocketStateMachine;

public class MQTT5 implements AgentSocketProtocol {

    @Override
    public void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, Context context, String filterSuffix) {

    }

    @Override
    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context) {

    }

}
