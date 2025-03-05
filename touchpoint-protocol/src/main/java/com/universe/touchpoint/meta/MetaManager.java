package com.universe.touchpoint.meta;

import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.meta.data.AgentActionMeta;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class MetaManager {

    public static  <C> AgentActionMeta buildAction(
            String receiverClassName,
            String agentName,
            LangModelConfig model,
            VisionModelConfig visionModelConfig,
            VisionLangModelConfig visionLangModelConfig,
            TransportConfig<C> transportConfig,
            String actionName,
            String actionDesc,
            ActionRole role,
            ActionMetricConfig actionMetricConfig,
            ActionDependency toActions) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(receiverClassName);

            Type[] interfaces = tpInstanceReceiverClass.getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            Type inputType = parameterizedType.getActualTypeArguments()[0];

            String inputClassName = null;
            inputClassName = inputType.getTypeName();
            String outputClassName = null;
            if (parameterizedType.getActualTypeArguments().length > 1) {
                Type outputType = parameterizedType.getActualTypeArguments()[1];
                outputClassName = outputType.getTypeName();
            }

            return new AgentActionMeta(actionName, agentName, receiverClassName, actionDesc, role, inputClassName, outputClassName, model, visionModelConfig, visionLangModelConfig, transportConfig, actionMetricConfig, toActions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
