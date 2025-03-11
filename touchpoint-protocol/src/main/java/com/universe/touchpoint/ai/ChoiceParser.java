package com.universe.touchpoint.ai;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public interface ChoiceParser<CH> {

  Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(CH choices, AgentAction<?, ?> currentAction);
}
