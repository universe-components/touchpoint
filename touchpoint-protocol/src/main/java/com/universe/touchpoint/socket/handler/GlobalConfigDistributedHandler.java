package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class GlobalConfigDistributedHandler implements AgentSocketStateHandler<Pair<TransportConfig<?>, AIModelConfig>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Pair<TransportConfig<?>, AIModelConfig> globalConfig, C agentContext, Context context, String task) {
        if (globalConfig != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig(globalConfig.first);
            TaskBuilder.task(task).getConfig().setModelConfig(globalConfig.second);
        }
        return true;
    }

}
