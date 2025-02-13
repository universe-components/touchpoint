package com.universe.touchpoint;

import android.content.Context;

import com.google.common.collect.Lists;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.annotations.AIModel;
import com.universe.touchpoint.annotations.Task;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.ApkUtils;
import com.universe.touchpoint.utils.ClassUtils;

import java.util.Map;
import java.util.Objects;

public class TaskProposer {

    public static void init(Context context) {
        if (Agent.isAnnotationPresent(Task.class)) {
            TransportConfig<?> transportConfigWrapper;
            Map<String, Map<String, Map<String, Object>>> taskProperties = ApkUtils.getFieldAnnotationValues(context,
                    Task.class, Lists.newArrayList(AIModel.class), "value", false);
            for (Map.Entry<String, Map<String, Map<String, Object>>> taskProperty : taskProperties.entrySet()) {
                AgentSocketStateMachine.getInstance().registerReceiver(context, new TaskContext(taskProperty.getKey()));
                AgentSocketStateMachine.getInstance().start(context, taskProperty.getKey());
                try {
                    transportConfigWrapper = (TransportConfig<?>) AnnotationUtils.annotation2Config(
                            Agent.getApplicationClass(), TransportConfigMapping.annotation2Config);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                TaskBuilder.task(taskProperty.getKey()).getConfig().setTransportConfig(transportConfigWrapper);
                for (Map.Entry<String, Map<String, Object>> property : taskProperty.getValue().entrySet()) {
                    if (Objects.equals(property.getKey(), "AIModel")) {
                        AIModelConfig aiModelConfig = TaskBuilder.task(taskProperty.getKey()).getConfig().getModelConfig();
                        ClassUtils.setProperties(aiModelConfig, property.getValue());
                    }
                }
            }
        }
    }

}
