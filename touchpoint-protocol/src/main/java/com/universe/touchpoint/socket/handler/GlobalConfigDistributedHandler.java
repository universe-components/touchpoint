package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.annotations.SocketProtocol;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import org.apache.commons.lang3.tuple.Triple;

public class GlobalConfigDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Object globalConfig, C agentContext, Context context, String task) {
        Triple<TransportConfig<?>, AIModelConfig, SocketProtocol> config = (Triple<TransportConfig<?>, AIModelConfig, SocketProtocol>) globalConfig;
        if (config != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig(config.getLeft());
            TaskBuilder.task(task).getConfig().setModelConfig(config.getMiddle());
            TaskBuilder.task(task).getConfig().setSocketProtocol(config.getRight());
        }
        return true;
    }

}
