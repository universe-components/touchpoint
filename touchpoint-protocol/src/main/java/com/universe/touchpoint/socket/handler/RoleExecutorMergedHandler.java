package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.rolemodel.RoleExecutorManager;
import com.universe.touchpoint.rolemodel.TaskExecutorFactory;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RoleExecutorMergedHandler implements AgentSocketStateHandler<RoleExecutorManager, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(RoleExecutorManager roleExecutorManager, C agentContext, Context context, String task) {
        TaskExecutorFactory.getInstance(task).getExecutorMap().putAll(roleExecutorManager.getExecutorMap());
        return true;
    }

}
