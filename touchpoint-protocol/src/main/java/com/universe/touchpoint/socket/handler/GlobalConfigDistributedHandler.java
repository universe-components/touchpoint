package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class GlobalConfigDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Object globalConfig, C agentContext, Context context, String task) {
        Pair<TransportConfig<?>, AIModelConfig> config = (Pair<TransportConfig<?>, AIModelConfig>) globalConfig;
        if (config != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig(config.first);
            TaskBuilder.task(task).getConfig().setModelConfig(config.second);
        }
        return true;
    }

}
