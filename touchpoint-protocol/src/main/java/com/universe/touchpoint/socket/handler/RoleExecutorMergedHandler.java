package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutor;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorMergedHandler<E> implements AgentSocketStateHandler<RoleExecutor<E>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(RoleExecutor<E> roleExecutor, C agentContext, Context context, String task) {
        RoleExecutorFactory.getInstance(task).getExecutorMap().putAll(roleExecutor.getExecutorMap());
        return true;
    }

}
