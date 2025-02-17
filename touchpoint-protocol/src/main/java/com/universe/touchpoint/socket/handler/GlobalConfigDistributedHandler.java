package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

public class GlobalConfigDistributedHandler<TC> implements AgentSocketStateHandler<Pair<TransportConfig<TC>, AIModelConfig>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Pair<TransportConfig<TC>, AIModelConfig> globalConfig, C actionContext, Context context, String task) {
        if (globalConfig != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig(globalConfig.first);
            TaskBuilder.task(task).getConfig().setModelConfig(globalConfig.second);

            TransportConfig<TC> transportConfig = ConfigManager.selectTransport(
                    ((TaskActionContext) actionContext).getAction(), actionContext.getBelongTask());
            TouchPointTransportRegistry<TC> registry =
                    (TouchPointTransportRegistry<TC>) TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());
            registry.init(context, transportConfig.config());
        }
        return true;
    }

}
