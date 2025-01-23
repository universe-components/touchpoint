package com.universe.touchpoint.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPointListener;
import com.universe.touchpoint.ai.AIModelResponse;

public interface AgentFinishListener extends TouchPointListener<AIModelResponse.AgentFinish, Boolean> {

    @Override
    Boolean onReceive(AIModelResponse.AgentFinish touchPoint, Context context);

}
