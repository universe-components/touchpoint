package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import org.apache.commons.lang3.tuple.Triple;

public class GlobalConfigDistributedHandler<TC> implements AgentSocketStateHandler<Triple<TransportConfig<TC>, AIModelConfig, ActionMetricConfig>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Triple<TransportConfig<TC>, AIModelConfig, ActionMetricConfig> globalConfig, C actionContext, Context context, String task) {
        if (globalConfig != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig(globalConfig.getLeft());
            TaskBuilder.task(task).getConfig().setModelConfig(globalConfig.getMiddle());
            TaskBuilder.task(task).getConfig().setActionMetricConfig(globalConfig.getRight());

            TransportConfig<TC> transportConfig = ConfigManager.selectTransport(
                    ((TaskActionContext) actionContext).getAction(), actionContext.getBelongTask());
            TouchPointTransportRegistry<TC> registry =
                    (TouchPointTransportRegistry<TC>) TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());
            registry.init(context, transportConfig.config());
        }
        return true;
    }

}
