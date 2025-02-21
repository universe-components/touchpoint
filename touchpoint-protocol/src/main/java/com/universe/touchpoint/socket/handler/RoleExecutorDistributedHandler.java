package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutorManager;
import com.universe.touchpoint.rolemodel.TaskExecutorFactory;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorDistributedHandler implements AgentSocketStateHandler<RoleExecutorManager, RoleExecutorManager> {

    public <C extends AgentContext> RoleExecutorManager onStateChange(RoleExecutorManager roleExecutorManager, C actionContext, Context context, String task) {
        TaskExecutorFactory.getInstance(task).getExecutorMap().putAll(roleExecutorManager.getExecutorMap());
        return TaskExecutorFactory.getInstance(task);
    }

}
