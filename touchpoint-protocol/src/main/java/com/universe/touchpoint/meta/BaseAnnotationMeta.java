package com.universe.touchpoint.meta;

import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.mapping.ActionMetricConfigMapping;
import com.universe.touchpoint.config.mapping.AgentSocketConfigMapping;
import com.universe.touchpoint.config.mapping.LangModelConfigMapping;
import com.universe.touchpoint.config.mapping.MetricSocketConfigMapping;
import com.universe.touchpoint.config.mapping.TaskMetricConfigMapping;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.config.mapping.VisionLangModelConfigMapping;
import com.universe.touchpoint.config.mapping.VisionModelConfigMapping;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.utils.AnnotationUtils;
import java.util.Map;

public abstract class BaseAnnotationMeta {

    protected Map<String, Object> attributes;
    protected Class<?> importingClass;

    public BaseAnnotationMeta(Class<?> importingClass, Map<String, Object> attributes) {
        this.importingClass = importingClass;
        this.attributes = attributes;
    }

    public String getDescription() {
        return (String) attributes.get("desc");
    }

    public TransportConfig<?> getTransportConfig() throws Exception {
        Map<Transport, Object> transportConfigMap = AnnotationUtils.annotation2Config(
                importingClass,
                TransportConfigMapping.annotation2Config,
                TransportConfigMapping.annotation2Type
        );

        Transport transportType = transportConfigMap.keySet().iterator().next();
        Object transportConfig = transportConfigMap.get(transportType);

        return new TransportConfig<>(transportType, transportConfig);
    }

    public LangModelConfig getLangModel() throws Exception {
        return (LangModelConfig) AnnotationUtils.annotation2Config(
                importingClass,
                LangModelConfigMapping.annotation2Config);
    }

    public VisionModelConfig getVisionModel() throws Exception {
        return (VisionModelConfig) AnnotationUtils.annotation2Config(
                importingClass,
                VisionModelConfigMapping.annotation2Config);
    }

    public VisionLangModelConfig getVisionLangModel() throws Exception {
        return (VisionLangModelConfig) AnnotationUtils.annotation2Config(
                importingClass,
                VisionLangModelConfigMapping.annotation2Config);
    }

    public MetricSocketConfig getMetricSocketConfig() throws Exception {
        return (MetricSocketConfig) AnnotationUtils.annotation2Config(
                importingClass,
                MetricSocketConfigMapping.annotation2Config);
    }

    public AgentSocketConfig getAgentSocketConfig() throws Exception {
        return (AgentSocketConfig) AnnotationUtils.annotation2Config(
                importingClass,
                AgentSocketConfigMapping.annotation2Config);
    }

    public ActionMetricConfig getActionMetricConfig() throws Exception {
        return (ActionMetricConfig) AnnotationUtils.annotation2Config(
                importingClass,
                ActionMetricConfigMapping.annotation2Config);
    }

    public TaskMetricConfig getTaskMetricConfig() throws Exception {
        return (TaskMetricConfig) AnnotationUtils.annotation2Config(
                importingClass,
                TaskMetricConfigMapping.annotation2Config);
    }

}
