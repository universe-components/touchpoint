package com.universe.touchpoint.android;

import android.content.Context;
import com.google.common.collect.Lists;
import com.universe.touchpoint.android.utils.ApkUtils;
import com.universe.touchpoint.annotations.ai.AIModel;
import com.universe.touchpoint.annotations.ai.VisionLangModel;
import com.universe.touchpoint.annotations.ai.VisionModel;
import com.universe.touchpoint.annotations.metric.MonitorActionMetrics;
import com.universe.touchpoint.annotations.metric.MonitorTaskMetrics;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.AgentSocket;
import com.universe.touchpoint.annotations.task.Task;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.context.TaskContext;
import com.universe.touchpoint.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TaskProposer {

    public static void init(Context context) {
        if (Agent.isAnnotationPresent(Task.class)) {
            List<Class<? extends Annotation>> extractAnnotationClasses = Lists.newArrayList(
                    AIModel.class,
                    AgentSocket.class,
                    VisionModel.class,
                    VisionLangModel.class,
                    MonitorTaskMetrics.class,
                    MonitorActionMetrics.class);
            extractAnnotationClasses.addAll(TransportConfigMapping.getAnnotationClasses());

            List<String> transportAnnotationName = extractAnnotationClasses.stream()
                    .map(Class::getName)
                    .toList();

            Map<String, Map<String, Map<String, Object>>> taskProperties = ApkUtils.getFieldAnnotationValues(context,
                    Task.class, extractAnnotationClasses, "value", false);
            for (Map.Entry<String, Map<String, Map<String, Object>>> taskProperty : taskProperties.entrySet()) {
                AgentSocketStateMachine.registerInstance(taskProperty.getKey(), Objects.requireNonNull(ConfigManager.selectAgentSocket(taskProperty.getKey())).getBindProtocol());

                TaskMeta taskMeta = new TaskMeta(taskProperty.getKey());
                for (Map.Entry<String, Map<String, Object>> property : taskProperty.getValue().entrySet()) {
                    if (Objects.equals(property.getKey(), "LangModel")) {
                        LangModelConfig langModelConfig = new LangModelConfig();
                        ClassUtils.setProperties(langModelConfig, property.getValue());
                        taskMeta.setModel(langModelConfig);
                    }
                    if (Objects.equals(property.getKey(), "VisionModel")) {
                        VisionModelConfig visionModelConfig = new VisionModelConfig();
                        ClassUtils.setProperties(visionModelConfig, property.getValue());
                        taskMeta.setVisionModel(visionModelConfig);
                    }
                    if (Objects.equals(property.getKey(), "VisionLangModel")) {
                        VisionLangModelConfig visionLangModelConfig = new VisionLangModelConfig();
                        ClassUtils.setProperties(visionLangModelConfig, property.getValue());
                        taskMeta.setVisionLangModel(visionLangModelConfig);
                    }
                    if (transportAnnotationName.contains(property.getKey())) {
                        TransportConfig<?> transportConfig = new TransportConfig<>(null, null);
                        ClassUtils.setProperties(transportConfig.config(), property.getValue());
                    }
                    if (Objects.equals(property.getKey(), "AgentSocket")) {
                        AgentSocketConfig socketConfig = new AgentSocketConfig();
                        ClassUtils.setProperties(socketConfig, property.getValue());
                        taskMeta.setAgentSocketConfig(socketConfig);
                        AgentSocketStateMachine.getInstance(taskProperty.getKey()).socketProtocol().initialize(socketConfig);
                    }
                    if (Objects.equals(property.getKey(), "MonitorActionMetrics")) {
                        ActionMetricConfig actionMetricConfig = new ActionMetricConfig();
                        ClassUtils.setProperties(actionMetricConfig, property.getValue());
                        taskMeta.setActionMetricConfig(actionMetricConfig);
                    }
                    if (Objects.equals(property.getKey(), "MonitorTaskMetrics")) {
                        TaskMetricConfig taskMetricConfig = new TaskMetricConfig();
                        ClassUtils.setProperties(taskMetricConfig, property.getValue());
                        taskMeta.setTaskMetricConfig(taskMetricConfig);
                    }
                }
                ((MetaRegion) TouchPointMemory.getRegion(Region.META)).putTouchPointTask(taskProperty.getKey(), taskMeta);
                AgentSocketStateMachine.getInstance(taskProperty.getKey()).registerReceiver(new TaskContext(taskProperty.getKey()), RoleType.OWNER);
            }
        }
    }

}
