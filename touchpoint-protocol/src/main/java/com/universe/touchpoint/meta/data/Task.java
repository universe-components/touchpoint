package com.universe.touchpoint.meta.data;

import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.meta.BaseMeta;

public class Task extends BaseMeta {

    public Task(String name,
                String agentName,
                String className,
                String desc,
                LangModelConfig model,
                VisionModelConfig visionModel,
                VisionLangModelConfig visionLangModel,
                TransportConfig<?> transportConfig,
                MetricSocketConfig metricSocketConfig,
                AgentSocketConfig agentSocketConfig,
                TaskMetricConfig taskMetricConfig,
                ActionMetricConfig actionMetricConfig) {
        super(name, agentName, className, desc, model, visionModel, visionLangModel, transportConfig, metricSocketConfig, agentSocketConfig, taskMetricConfig, actionMetricConfig);
    }

}
