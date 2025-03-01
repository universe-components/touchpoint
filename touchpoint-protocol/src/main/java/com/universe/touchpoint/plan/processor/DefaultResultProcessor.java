package com.universe.touchpoint.plan.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ResultProcessor;
import java.util.List;

public class DefaultResultProcessor<T extends TouchPoint> implements ResultProcessor<T> {

    @Override
    public <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(T result, String goal, String task, Context context, Transport transport) {
        return null;
    }

}
