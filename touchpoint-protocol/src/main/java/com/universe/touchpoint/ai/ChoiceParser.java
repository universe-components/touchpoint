package com.universe.touchpoint.ai;

import android.util.Pair;

public interface ChoiceParser<T> {

    Pair<AIModelResponse.AgentAction, AIModelResponse.AgentFinish> parse(T choice);

}
