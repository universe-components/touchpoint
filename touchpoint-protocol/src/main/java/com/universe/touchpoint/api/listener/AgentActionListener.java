package com.universe.touchpoint.api.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.TouchPointListener;

public interface AgentActionListener<R extends TouchPoint> extends TouchPointListener<AgentAction, R> {

    R onReceive(AgentAction action, Context context);

}
