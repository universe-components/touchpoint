package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

import java.util.Map;

public class GlobalConfigReadyHandler<Config> implements AgentSocketStateHandler<Boolean, Map<String, Config>> {

    @Override
    public <C extends AgentContext> Map<String, Config> onStateChange(Boolean ready, C agentContext, Context context, String task) {
        if (ready) {
            return (Map<String, Config>) Map.of(
                    "transport", ConfigManager.selectTransport(null, task),
                    "langmodel", ConfigManager.selectModel(null, null, task),
                    "visionmodel", ConfigManager.selectVisionModel(null, null, task),
                    "visionLangModel", ConfigManager.selectVisionLangModel(null, null, task),
                    "actionMetric", ConfigManager.selectActionMetricConfig(null, task),
                    "taskMetric", ConfigManager.selectTaskMetricConfig(task)
            );
        }
        return null;
    }

}
