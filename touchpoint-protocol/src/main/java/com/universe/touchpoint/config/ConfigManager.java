package com.universe.touchpoint.config;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.meta.AgentActionMeta;
import com.universe.touchpoint.meta.AgentMeta;
import com.universe.touchpoint.meta.Task;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;

public class ConfigManager {

    public static LangModelConfig selectModel(String input, AgentActionMeta actionMeta, String task) {
        // 首先检查 action 中的模型
        if (actionMeta != null) {
            LangModelConfig modelConfig = actionMeta.getModel();
            if (modelConfig != null) {
                // 如果 action 中有模型，则直接使用该模型
                return actionMeta.getModel();
            }
        }

        // 如果 Agent.getModel() 也为空，再检查 AgentBuilder 中的配置
        LangModelConfig modelFromBuilder = TaskBuilder.getBuilder(task).getConfig().getModelConfig();
        if (modelFromBuilder != null) {
            return modelFromBuilder;
        }

        // 如果 action 中没有模型，再检查 Agent 的模型
        assert actionMeta != null;
        AgentMeta agentMeta = ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAgent(actionMeta.getAgentName());
        Model model = agentMeta.getModel().getModel();
        if (model != null) {
            float temperature = agentMeta.getModel().getTemperature();
            return new LangModelConfig(
                    model,
                    temperature,
                    model == Model.ClAUDE_3_5_SONNET ? AIModelType.ANTHROPIC : AIModelType.OPEN_AI
            );
        }

        // 如果都没有模型，则返回默认模型 OPEN_AI
        return new LangModelConfig(Model.o1, 0.0f, AIModelType.OPEN_AI);
    }

    public static VisionModelConfig selectVisionModel(String input, AgentActionMeta actionMeta, String task) {
        // 首先检查 action 中的模型
        if (actionMeta != null) {
            VisionModelConfig modelConfig = actionMeta.getVisionModel();
            if (modelConfig != null) {
                // 如果 action 中有模型，则直接使用该模型
                return actionMeta.getVisionModel();
            }
        }

        // 如果 Agent.getModel() 也为空，再检查 AgentBuilder 中的配置
        VisionModelConfig modelFromBuilder = TaskBuilder.getBuilder(task).getConfig().getVisionModelConfig();
        if (modelFromBuilder != null) {
            return modelFromBuilder;
        }

        // 如果 action 中没有模型，再检查 Agent 的模型
        assert actionMeta != null;
        AgentMeta agentMeta = ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAgent(actionMeta.getAgentName());
        Model model = agentMeta.getVisionModel().getModel();
        if (model != null) {
            float temperature = agentMeta.getVisionModel().getTemperature();
            return new VisionModelConfig(model, temperature);
        }

        return null;
    }

    public static VisionLangModelConfig selectVisionLangModel(String input, AgentActionMeta actionMeta, String task) {
        // 首先检查 action 中的模型
        if (actionMeta != null) {
            VisionLangModelConfig modelConfig = actionMeta.getVisionLangModel();
            if (modelConfig != null) {
                // 如果 action 中有模型，则直接使用该模型
                return actionMeta.getVisionLangModel();
            }
        }

        // 如果 Agent.getModel() 也为空，再检查 AgentBuilder 中的配置
        VisionLangModelConfig modelFromBuilder = TaskBuilder.getBuilder(task).getConfig().getVisionLangModelConfig();
        if (modelFromBuilder != null) {
            return modelFromBuilder;
        }

        // 如果 action 中没有模型，再检查 Agent 的模型
        AgentMeta agentMeta = ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAgent(actionMeta.getAgentName());
        Model model = agentMeta.getVisionLangModel().getModel();
        if (model != null) {
            float temperature = agentMeta.getVisionLangModel().getTemperature();
            return new VisionLangModelConfig(model, temperature);
        }

        return null;
    }

    public static <C> TransportConfig<C> selectTransport(String action, String task) {
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        AgentActionMeta actionMeta = metaRegion.getTouchPointAction(action);
        TransportConfig<C> config = (TransportConfig<C>) actionMeta.getTransportConfig();

        if (config != null) {
            return (TransportConfig<C>) actionMeta.getTransportConfig();
        }

        TransportConfig<?> transportConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getTransportConfig();
        if (transportConfigFromTask != null) {
            return (TransportConfig<C>) transportConfigFromTask;
        }

        try {
            AgentMeta agentMeta = ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAgent(actionMeta.getAgentName());
            TransportConfig<C> transportConfig = (TransportConfig<C>) agentMeta.getTransportConfig();

            if (transportConfig != null) {
                return transportConfig;
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
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            Task taskMeta = metaRegion.getTouchPointTask(task);
            AgentMeta agentMeta = metaRegion.getTouchPointAgent(taskMeta.getAgentName());
            AgentSocketConfig socketConfig = agentMeta.getAgentSocketConfig();

            if (socketConfig != null) {
                return socketConfig;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static MetricSocketConfig selectMetricSocket(String task) {
        MetricSocketConfig socketConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getMetricSocketConfig();
        if (socketConfigFromTask != null) {
            return socketConfigFromTask;
        }

        try {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            Task taskMeta = metaRegion.getTouchPointTask(task);
            AgentMeta agentMeta = metaRegion.getTouchPointAgent(taskMeta.getAgentName());
            MetricSocketConfig socketConfig = agentMeta.getMetricSocketConfig();

            if (socketConfig != null) {
                return socketConfig;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static TaskMetricConfig selectTaskMetricConfig(String task) {
        TaskMetricConfig taskMetricConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getTaskMetricConfig();
        if (taskMetricConfigFromTask != null) {
            return taskMetricConfigFromTask;
        }

        try {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            Task taskMeta = metaRegion.getTouchPointTask(task);
            AgentMeta agentMeta = metaRegion.getTouchPointAgent(taskMeta.getAgentName());
            TaskMetricConfig taskMetricConfigFromAgent = agentMeta.getTaskMetricConfig();

            if (taskMetricConfigFromAgent != null) {
                return taskMetricConfigFromAgent;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public static ActionMetricConfig selectActionMetricConfig(String action, String task) {
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        AgentActionMeta actionMeta = metaRegion.getTouchPointAction(action);
        ActionMetricConfig config = actionMeta.getActionMetricConfig();

        if (config != null) {
            return actionMeta.getActionMetricConfig();
        }

        ActionMetricConfig actionMetricConfigFromTask = TaskBuilder.getBuilder(task).getConfig().getActionMetricConfig();
        if (actionMetricConfigFromTask != null) {
            return actionMetricConfigFromTask;
        }

        try {
            AgentMeta agentMeta = metaRegion.getTouchPointAgent(actionMeta.getAgentName());
            ActionMetricConfig actionMetricConfigFromAgent = agentMeta.getActionMetricConfig();

            if (actionMetricConfigFromAgent != null) {
                return actionMetricConfigFromAgent;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
