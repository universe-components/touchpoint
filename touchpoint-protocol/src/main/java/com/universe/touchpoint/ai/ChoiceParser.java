package com.universe.touchpoint.ai;

import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;

import java.util.List;
import java.util.Map;

public interface ChoiceParser<C, R> {

    <I extends TouchPoint, O extends TouchPoint> Pair<List<AgentAction<I, O>>, AgentFinish> parse(Map<C, List<R>> choices);

}
