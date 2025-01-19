package com.universe.touchpoint.dispatcher;

import android.util.Pair;

import com.universe.touchpoint.ai.AIModelResponse;

public interface ChoiceParser<T> {

    Pair<AIModelResponse.AgentAction, AIModelResponse.AgentFinish> parse(T choice);

}
