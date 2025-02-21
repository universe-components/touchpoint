package com.universe.touchpoint.driver;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.Transport;

import java.util.List;

public interface ResultProcessor<R> {

    <I extends TouchPoint, O extends TouchPoint> Pair<List<AgentAction<I, O>>, AgentFinish> process(R result, String goal, String task, Context context, Transport transport);

}
