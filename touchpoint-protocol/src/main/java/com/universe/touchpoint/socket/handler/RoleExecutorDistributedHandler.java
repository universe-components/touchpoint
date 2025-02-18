package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutor;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorDistributedHandler<E> implements AgentSocketStateHandler<RoleExecutor<E>, RoleExecutor<E>> {

    public <C extends AgentContext> RoleExecutor<E> onStateChange(RoleExecutor<E> roleExecutor, C actionContext, Context context, String task) {
        RoleExecutorFactory.getInstance(task).getExecutorMap().putAll(roleExecutor.getExecutorMap());
        return RoleExecutorFactory.getInstance(task);
    }

}
