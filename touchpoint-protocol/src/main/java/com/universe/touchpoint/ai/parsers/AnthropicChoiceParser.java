package com.universe.touchpoint.ai.parsers;

import android.util.Pair;

import com.anthropic.models.Completion;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.ai.ChoiceParser;

import java.util.List;
import java.util.Map;

public class AnthropicChoiceParser implements ChoiceParser<Completion, String> {

    @Override
    public Pair<List<AIModelResponse.AgentAction>, AIModelResponse.AgentFinish> parse(Map<Completion, List<String>> choices) {
        return null;
    }

}
