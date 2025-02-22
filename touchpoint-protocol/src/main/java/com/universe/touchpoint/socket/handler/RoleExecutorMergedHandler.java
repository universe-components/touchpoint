package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutorContainer;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorMergedHandler implements AgentSocketStateHandler<RoleExecutorContainer, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(RoleExecutorContainer roleExecutorContainer, C agentContext, Context context, String task) {
        TaskRoleExecutor.getInstance(task).getExecutorMap().putAll(roleExecutorContainer.getExecutorMap());
        return true;
    }

}
