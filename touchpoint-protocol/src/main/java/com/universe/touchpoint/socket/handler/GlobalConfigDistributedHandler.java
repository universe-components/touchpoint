package com.universe.touchpoint.socket.handler;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
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
    public <C extends AgentContext> Boolean onStateChange(Map<String, Config> globalConfig, C actionContext, String task) {
        if (globalConfig != null) {
            TaskBuilder.task(task).getConfig().setTransportConfig((TransportConfig<?>) globalConfig.get("transport"));
            TaskBuilder.task(task).getConfig().setModelConfig((LangModelConfig) globalConfig.get("langmodel"));
            TaskBuilder.task(task).getConfig().setVisionModelConfig((VisionModelConfig) globalConfig.get("visionmodel"));
            TaskBuilder.task(task).getConfig().setVisionLangModelConfig((VisionLangModelConfig) globalConfig.get("visionLangModel"));
            TaskBuilder.task(task).getConfig().setActionMetricConfig((ActionMetricConfig) globalConfig.get("actionMetric"));
            TaskBuilder.task(task).getConfig().setTaskMetricConfig((TaskMetricConfig) globalConfig.get("taskMetric"));

            TransportConfig<TC> transportConfig = ConfigManager.selectTransport(
                    ((TaskActionContext) actionContext).getAction(), actionContext.getBelongTask());
            TouchPointTransportRegistry<TC> registry =
                    (TouchPointTransportRegistry<TC>) TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());
            registry.init(transportConfig.config());
        }
        return true;
    }

}
