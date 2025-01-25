package com.universe.touchpoint.ai;

import android.util.Pair;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;

import java.util.List;
import java.util.Map;

public interface ChoiceParser<C, R> {

    Pair<List<AgentAction>, AgentFinish> parse(Map<C, List<R>> choices);

}
