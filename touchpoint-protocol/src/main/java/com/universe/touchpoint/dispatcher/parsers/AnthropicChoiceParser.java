package com.universe.touchpoint.dispatcher.parsers;

import android.util.Pair;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.dispatcher.ChoiceParser;

public class AnthropicChoiceParser implements ChoiceParser<String> {

    @Override
    public Pair<AIModelResponse.AgentAction, AIModelResponse.AgentFinish> parse(String choice) {
        return null;
    }

}
