package com.universe.touchpoint.socket.handler;

import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.context.TaskActionContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;
import java.util.Map;

public class GlobalConfigReadyHandler<Config, TC> implements AgentSocketStateHandler<Map<String, Config>, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Map<String, Config> globalConfig, C actionContext, String task) {
        if (globalConfig != null) {
            TaskMeta taskMeta = new TaskMeta(task);
            taskMeta.setTransportConfig((TransportConfig<?>) globalConfig.get("transport"));
            taskMeta.setModel((LangModelConfig) globalConfig.get("langmodel"));
            taskMeta.setVisionModel((VisionModelConfig) globalConfig.get("visionmodel"));
            taskMeta.setVisionLangModel((VisionLangModelConfig) globalConfig.get("visionLangModel"));
            taskMeta.setActionMetricConfig((ActionMetricConfig) globalConfig.get("actionMetric"));
            taskMeta.setTaskMetricConfig((TaskMetricConfig) globalConfig.get("taskMetric"));
            ((MetaRegion) TouchPointMemory.getRegion(Region.META)).putTouchPointTask(task, taskMeta);

            TransportConfig<TC> transportConfig = ConfigManager.selectTransport(
                    ((TaskActionContext) actionContext).getAction(), actionContext.getBelongTask());
            TouchPointTransportRegistry<TC> registry =
                    (TouchPointTransportRegistry<TC>) TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());
            registry.init(transportConfig.config());
        }
        return true;
    }

}
