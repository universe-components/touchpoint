package com.universe.touchpoint.ai;

import android.util.Pair;

import java.util.List;
import java.util.Map;

public interface ChoiceParser<C, R> {

    Pair<List<AIModelResponse.AgentAction>, AIModelResponse.AgentFinish> parse(Map<C, List<R>> choices);

}
