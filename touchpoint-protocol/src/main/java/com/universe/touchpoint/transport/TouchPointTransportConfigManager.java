package com.universe.touchpoint.transport;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.ai.AIModel;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.ai.models.Anthropic;
import com.universe.touchpoint.ai.models.OpenAI;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class TouchPointTransportConfigManager {

    private static final Map<AIModelType, Class<? extends AIModel<?, ?, ?>>> modelMap = new HashMap<>();
    static {
        modelMap.put(AIModelType.OPEN_AI, OpenAI.class);
        modelMap.put(AIModelType.ANTHROPIC, Anthropic.class);
    }

    public static <T> TransportConfig<T> agentConfig(Transport transport) {
        try {
            Class<?> configClass = TransportConfigMapping.transport2Config.get(transport);
            assert configClass != null;
            String annotationClassName = "com.universe.touchpoint.annotations." + configClass.getSimpleName();
            Class<?> annotationClass = Class.forName(annotationClassName);

            String applicationName = (String) Agent.getProperty("applicationName", (Class<Annotation>) annotationClass);
            if (applicationName != null) {
                String registryAddress = (String) Agent.getProperty("registryAddress", (Class<Annotation>) annotationClass);
                return (TransportConfig<T>) new TransportConfig<>(
                        transport,
                        configClass.getDeclaredConstructor(String.class, String.class).newInstance(applicationName, registryAddress));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (TransportConfig<T>) AgentBuilder.getBuilder().getConfig().getTransportConfig();
    }

    public static <T> void registerAgentTransportConfig(T transportConfig, Context context) {
        String transportConfigAction = TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_FILTER_NAME);

        Intent transportConfigIntent = new Intent(transportConfigAction);
        transportConfigIntent.putExtra(TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_EVENT_NAME,
                SerializeUtils.serializeToByteArray(transportConfig));

        context.sendBroadcast(transportConfigIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void registerTransportConfigReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_FILTER_NAME));
        context.registerReceiver(new TouchPointTransportConfigBroadcastReceiver(), filter, Context.RECEIVER_EXPORTED);
    }

}
