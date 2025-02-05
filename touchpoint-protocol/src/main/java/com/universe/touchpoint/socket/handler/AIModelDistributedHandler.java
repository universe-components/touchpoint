package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class AIModelDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @Override
    public Boolean onStateChange(Object aiModelConfig, Context context, String task) {
        if (aiModelConfig != null) {
            AgentBuilder.getBuilder().getConfig().setModelConfig((AIModelConfig) aiModelConfig);
        }
        return true;
    }

}
