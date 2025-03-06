package com.universe.touchpoint.plan.processor;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ResultProcessor;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public class DefaultResultProcessor<T extends TouchPoint> implements ResultProcessor<T> {

    @Override
    public <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(T result, String goal, String task, Transport transport) {
        return null;
    }

}
