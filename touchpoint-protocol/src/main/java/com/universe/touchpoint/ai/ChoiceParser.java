package com.universe.touchpoint.ai;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;

import org.apache.commons.lang3.tuple.Pair;
import java.util.List;

public interface ChoiceParser<CH> {

    Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(CH choices, AgentAction<?, ?> currentAction);

}
