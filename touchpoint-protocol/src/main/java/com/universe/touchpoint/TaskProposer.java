package com.universe.touchpoint;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.common.collect.Lists;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.annotations.ai.AIModel;
import com.universe.touchpoint.annotations.socket.AgentSocket;
import com.universe.touchpoint.annotations.task.Task;
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.metric.TaskMetricConfig;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.socket.context.TaskContext;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.utils.ApkUtils;
import com.universe.touchpoint.utils.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TaskProposer {

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public static void init(Context context) {
        if (Agent.isAnnotationPresent(Task.class)) {
            List<Class<? extends Annotation>> extractAnnotationClasses = Lists.newArrayList(AIModel.class, AgentSocket.class);
            extractAnnotationClasses.addAll(TransportConfigMapping.getAnnotationClasses());

            List<String> transportAnnotationName = extractAnnotationClasses.stream()
                    .map(Class::getName)
                    .toList();

            Map<String, Map<String, Map<String, Object>>> taskProperties = ApkUtils.getFieldAnnotationValues(context,
                    Task.class, extractAnnotationClasses, "value", false);
            for (Map.Entry<String, Map<String, Map<String, Object>>> taskProperty : taskProperties.entrySet()) {
                AgentSocketStateMachine.registerInstance(taskProperty.getKey(), Objects.requireNonNull(ConfigManager.selectAgentSocket(taskProperty.getKey())).getBindProtocol());

                for (Map.Entry<String, Map<String, Object>> property : taskProperty.getValue().entrySet()) {
                    if (Objects.equals(property.getKey(), "AIModel")) {
                        AIModelConfig aiModelConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getModelConfig();
                        ClassUtils.setProperties(aiModelConfig, property.getValue());
                    }
                    if (transportAnnotationName.contains(property.getKey())) {
                        TransportConfig<?> transportConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getTransportConfig();
                        ClassUtils.setProperties(transportConfig.config(), property.getValue());
                    }
                    if (Objects.equals(property.getKey(), "AgentSocket")) {
                        AgentSocketConfig socketConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getSocketConfig();
                        ClassUtils.setProperties(socketConfig, property.getValue());
                        AgentSocketStateMachine.getInstance(taskProperty.getKey()).socketProtocol().initialize(socketConfig);
                    }
                    if (Objects.equals(property.getKey(), "MonitorActionMetrics")) {
                        ActionMetricConfig actionMetricConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getActionMetricConfig();
                        ClassUtils.setProperties(actionMetricConfig, property.getValue());
                    }
                    if (Objects.equals(property.getKey(), "MonitorTaskMetrics")) {
                        TaskMetricConfig taskMetricConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getTaskMetricConfig();
                        ClassUtils.setProperties(taskMetricConfig, property.getValue());
                    }
                }

                AgentSocketStateMachine.getInstance(taskProperty.getKey()).registerReceiver(context, new TaskContext(taskProperty.getKey()));
            }
        }
    }

}
