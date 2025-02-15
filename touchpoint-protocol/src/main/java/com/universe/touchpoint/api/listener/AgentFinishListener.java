package com.universe.touchpoint.api.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;

public interface AgentFinishListener extends TouchPointListener<AgentFinish, TouchPoint> {

    @Override
    TouchPoint onReceive(AgentFinish touchPoint, Context context);

}
