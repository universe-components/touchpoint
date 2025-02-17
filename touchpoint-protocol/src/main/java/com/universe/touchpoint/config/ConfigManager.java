package com.universe.touchpoint.config;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.annotations.ai.AIModel;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.config.mapping.ActionMetricConfigMapping;
import com.universe.touchpoint.config.mapping.AgentSocketConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.task.ActionMetricConfig;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.utils.AnnotationUtils;

import java.util.Map;

public class ConfigManager {

    public static AIModelConfig selectModel(String input, AgentActionMetaInfo actionMeta, String task) {
        // 首先检查 action 中的模型
        if (actionMeta != null) {
            AIModelConfig modelConfig = actionMeta.getModel();
            if (modelConfig != null) {
                // 如果 action 中有模型，则直接使用该模型
                return actionMeta.getModel();
            }
        }

        // 如果 Agent.getModel() 也为空，再检查 AgentBuilder 中的配置
        AIModelConfig modelFromBuilder = TaskBuilder.getBuilder(task).getConfig().getModelConfig();
        if (modelFromBuilder != null) {
            return modelFromBuilder;
        }

        // 如果 action 中没有模型，再检查 Agent 的模型
        Model model = (Model) Agent.getProperty("model", AIModel.class);
        if (model != null) {
            float temperature = (float) Agent.getProperty("temperature", AIModel.class);
            return new AIModelConfig(
                    model,
                    temperature,
                    model == Model.ClAUDE_3_5_SONNET ? AIModelType.ANTHROPIC : AIModelType.OPEN_AI
            );
        }

        // 如果都没有模型，则返回默认模型 OPEN_AI
        return new AIModelConfig(Model.o1, 0.0f, AIModelType.OPEN_AI);
    }

    public static <C> TransportConfig<C> selectTransport(String action, String task) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        AgentActionMetaInfo actionMeta = driverRegion.getTouchPointAction(action);
        TransportConfig<C> config = (TransportConfig<C>) actionMeta.getTransportConfig();

        if (config != null) {
            return (TransportConfig<C>) actionMeta.getTransportConfig();
        }

        TransportConfig<?> transportConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getTransportConfig();
        if (transportConfigFromTask != null) {
            return (TransportConfig<C>) transportConfigFromTask;
        }

        try {
            Map<Transport, C> transportConfigMap = (Map<Transport, C>) AnnotationUtils.annotation2Config(
                    Agent.getApplicationClass(),
                    TransportConfigMapping.annotation2Config,
                    TransportConfigMapping.annotation2Type);

            if (!transportConfigMap.isEmpty()) {
                return (TransportConfig<C>) transportConfigMap.get(transportConfigMap.keySet().iterator().next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new TransportConfig<>(Transport.BROADCAST, null);
    }

    public static AgentSocketConfig selectAgentSocket(String task) {
        AgentSocketConfig socketConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getSocketConfig();
        if (socketConfigFromTask != null) {
            return socketConfigFromTask;
        }

        try {
            AgentSocketConfig socketConfig = (AgentSocketConfig) AnnotationUtils.annotation2Config(
                    Agent.getApplicationClass(),
                    AgentSocketConfigMapping.annotation2Config);

            if (socketConfig != null) {
                return socketConfig;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static ActionMetricConfig selectActionMetricConfig(String action, String task) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        AgentActionMetaInfo actionMeta = driverRegion.getTouchPointAction(action);
        ActionMetricConfig config = actionMeta.getActionMetricConfig();

        if (config != null) {
            return actionMeta.getActionMetricConfig();
        }

        ActionMetricConfig actionMetricConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getActionMetricConfig();
        if (actionMetricConfigFromTask != null) {
            return actionMetricConfigFromTask;
        }

        try {
            ActionMetricConfig actionMetricConfigFromAgent = (ActionMetricConfig) AnnotationUtils.annotation2Config(
                    Agent.getApplicationClass(),
                    ActionMetricConfigMapping.annotation2Config);

            if (actionMetricConfigFromAgent != null) {
                return actionMetricConfigFromAgent;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
