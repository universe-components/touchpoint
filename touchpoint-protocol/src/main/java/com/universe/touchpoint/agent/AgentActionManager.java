package com.universe.touchpoint.agent;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.config.ai.VisionLangModelConfig;
import com.universe.touchpoint.config.ai.VisionModelConfig;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.task.ActionDependency;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.metric.ActionMetricConfig;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.transport.broadcast.TouchPointBroadcastReceiver;

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

    public <C> void extractAndRegisterAction(
            String receiverClassName,
            LangModelConfig model,
            VisionModelConfig visionModelConfig,
            VisionLangModelConfig visionLangModelConfig,
            TransportConfig<C> transportConfig,
            String actionName,
            String actionDesc,
            ActionRole role,
            String agentName,
            ActionMetricConfig actionMetricConfig,
            ActionDependency toActions) {
        try {
            Class<?> tpInstanceReceiverClass = Class.forName(receiverClassName);

            Type[] interfaces = tpInstanceReceiverClass.getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) interfaces[0];
            Type inputType = parameterizedType.getActualTypeArguments()[0];

            String inputClassName = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                inputClassName = inputType.getTypeName();
            }
            String outputClassName = null;
            if (parameterizedType.getActualTypeArguments().length > 1) {
                Type outputType = parameterizedType.getActualTypeArguments()[1];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    outputClassName = outputType.getTypeName();
                }
            }

            AgentActionMetaInfo agentActionMetaInfo = new AgentActionMetaInfo(actionName, receiverClassName, actionDesc, role, inputClassName, outputClassName, model, visionModelConfig, visionLangModelConfig, transportConfig, actionMetricConfig, toActions);
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointAction(
                    TouchPointHelper.touchPointActionName(actionName, agentName), agentActionMetaInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void registerAgentFinishReceiver(Context appContext, String filter) {
        TouchPointBroadcastReceiver<? extends TouchPoint, ?, ?> agentFinishReceiver = new TouchPointBroadcastReceiver<>(AgentFinish.class, appContext);

        IntentFilter agentFinishFilter = new IntentFilter(TouchPointHelper.touchPointFilterName(filter));
        appContext.registerReceiver(agentFinishReceiver, agentFinishFilter, Context.RECEIVER_EXPORTED);
    }

    @SuppressWarnings("unchecked")
    public <T> T paddingActionInput(String actionName, String actionInput, String agentName) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        AgentActionMetaInfo agentActionMetaInfo = driverRegion.getTouchPointAction(TouchPointHelper.touchPointActionName(actionName, agentName));
        Class<T> inputClass;
        try {
            inputClass = (Class<T>) Class.forName(agentActionMetaInfo.getInputClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // 分割输入
        ModelOutputDecoder<T, T> actionParamsDecoder = (ModelOutputDecoder<T, T>) ModelOutputDecoderSelector.selectParamsDecoder(agentActionMetaInfo.getType());
        return actionParamsDecoder.run(actionInput, inputClass);
    }

}
