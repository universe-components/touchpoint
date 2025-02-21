package com.universe.touchpoint.driver.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultProcessor;

import java.util.List;

public class DefaultResultProcessor<T extends TouchPoint> implements ResultProcessor<T> {

    @Override
    public <I extends TouchPoint, O extends TouchPoint> Pair<List<AgentAction<I, O>>, AgentFinish> process(T result, String goal, String task, Context context, Transport transport) {
        // TODO add rule driven implementation
        return null;
    }

}
