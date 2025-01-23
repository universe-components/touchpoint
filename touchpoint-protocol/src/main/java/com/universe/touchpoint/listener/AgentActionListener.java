package com.universe.touchpoint.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointListener;
import com.universe.touchpoint.ai.AIModelResponse;

public interface AgentActionListener<R extends TouchPoint> extends TouchPointListener<AIModelResponse.AgentAction, R> {

    R onReceive(AIModelResponse.AgentAction action, Context context);

}
