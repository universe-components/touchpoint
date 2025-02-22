package com.universe.touchpoint.ai.parsers;

import android.util.Pair;

import com.anthropic.models.Completion;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;

import java.util.List;
import java.util.Map;

public class AnthropicChoiceParser implements ChoiceParser<Completion, String> {

    @Override
    public <ReqInput extends TouchPoint, ReqOutput extends TouchPoint, RespInput extends TouchPoint, RespOutput extends TouchPoint> Pair<List<AgentAction<RespInput, RespOutput>>, AgentFinish> parse(Map<Completion, List<String>> choices, AgentAction<ReqInput, ReqOutput> currentAction) {
        return null;
    }

}
