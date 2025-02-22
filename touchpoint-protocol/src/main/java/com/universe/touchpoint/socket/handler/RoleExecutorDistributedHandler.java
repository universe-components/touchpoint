package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutorContainer;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorDistributedHandler implements AgentSocketStateHandler<RoleExecutorContainer, RoleExecutorContainer> {

    public <C extends AgentContext> RoleExecutorContainer onStateChange(RoleExecutorContainer roleExecutorContainer, C actionContext, Context context, String task) {
        TaskRoleExecutor.getInstance(task).getExecutorMap().putAll(roleExecutorContainer.getExecutorMap());
        return TaskRoleExecutor.getInstance(task);
    }

}
