package com.universe.touchpoint.agent;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class AgentActionManager {

    private static AgentActionManager actionManager;
    private static final Object lock = new Object();

    public static AgentActionManager getInstance() {
        synchronized (lock) {
            if (actionManager == null) {
                actionManager = new AgentActionManager();
            }
            return actionManager;
        }
    }

    public <C> AgentActionMeta buildAction(
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

    public void registerAgentFinishReceiver(String filter) {
//        TouchPointBroadcastReceiver<? extends TouchPoint> agentFinishReceiver = new TouchPointBroadcastReceiver<>(AgentFinish.class);
//
//        IntentFilter agentFinishFilter = new IntentFilter(TouchPointHelper.touchPointFilterName(filter));
//        appContext.registerReceiver(agentFinishReceiver, agentFinishFilter, Context.RECEIVER_EXPORTED);
    }

    @SuppressWarnings("unchecked")
    public <T> T paddingActionInput(String actionName, String actionInput) {
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        AgentActionMeta agentActionMeta = metaRegion.getTouchPointAction(actionName);
        Class<T> inputClass;
        try {
            inputClass = (Class<T>) Class.forName(agentActionMeta.getInputClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 分割输入
        AIModelOutputDecoder<T, T> actionParamsDecoder = (AIModelOutputDecoder<T, T>) AIModelOutputDecoderSelector.selectParamsDecoder(agentActionMeta.getType());
        return actionParamsDecoder.run(actionInput, inputClass);
    }

}
