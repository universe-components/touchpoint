package com.universe.touchpoint.api.listener;

import android.content.Context;

import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;

public interface AgentFinishListener extends TouchPointListener<AgentFinish, Boolean> {

    @Override
    Boolean onReceive(AgentFinish touchPoint, Context context);

}
