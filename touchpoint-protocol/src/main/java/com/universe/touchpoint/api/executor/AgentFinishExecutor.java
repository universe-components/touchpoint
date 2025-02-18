package com.universe.touchpoint.api.executor;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointExecutor;

public interface AgentFinishExecutor extends TouchPointExecutor<AgentFinish, TouchPoint> {

    @Override
    TouchPoint run(AgentFinish touchPoint, Context context);

}
