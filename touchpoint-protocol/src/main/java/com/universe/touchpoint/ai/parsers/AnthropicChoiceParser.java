package com.universe.touchpoint.ai.parsers;

import android.util.Pair;

import com.anthropic.models.Completion;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;

import java.util.List;
import java.util.Map;

public class AnthropicChoiceParser implements ChoiceParser<Completion, String> {

    @Override
    public <I extends TouchPoint, O extends TouchPoint> Pair<List<AgentAction<I, O>>, AgentFinish> parse(Map<Completion, List<String>> choices) {
        return null;
    }

}
