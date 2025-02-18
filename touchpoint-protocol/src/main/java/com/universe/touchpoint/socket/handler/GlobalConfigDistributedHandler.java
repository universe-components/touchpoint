package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.context.TaskActionContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import java.util.Map;

public class GlobalConfigDistributedHandler<Config, TC> implements AgentSocketStateHandler<Map<String, Config>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Map<String, Config> globalConfig, C actionContext, Context context, String task) {
        if (globalConfig != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig((TransportConfig<?>) globalConfig.get("transport"));
            TaskBuilder.task(task).getConfig().setModelConfig((AIModelConfig) globalConfig.get("aimodel"));
            TaskBuilder.task(task).getConfig().setActionMetricConfig((ActionMetricConfig) globalConfig.get("actionMetric"));
            TaskBuilder.task(task).getConfig().setTaskMetricConfig((TaskMetricConfig) globalConfig.get("taskMetric"));

            TransportConfig<TC> transportConfig = ConfigManager.selectTransport(
                    ((TaskActionContext) actionContext).getAction(), actionContext.getBelongTask());
            TouchPointTransportRegistry<TC> registry =
                    (TouchPointTransportRegistry<TC>) TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());
            registry.init(context, transportConfig.config());
        }
        return true;
    }

}
