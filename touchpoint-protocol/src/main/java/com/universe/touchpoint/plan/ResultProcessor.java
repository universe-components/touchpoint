package com.universe.touchpoint.plan;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;
import java.util.List;

public interface ResultProcessor<R> {

    <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(R result, String goal, String task, Context context, Transport transport);

}
