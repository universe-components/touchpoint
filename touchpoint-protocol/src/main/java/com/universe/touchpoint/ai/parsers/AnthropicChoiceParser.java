package com.universe.touchpoint.ai.parsers;

import com.anthropic.models.Completion;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;

public class AnthropicChoiceParser implements ChoiceParser<Map<Completion, String>> {

    @Override
    public Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(Map<Completion, String> choices, AgentAction<?, ?> currentAction) {
        return null;
    }

}
