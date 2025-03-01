package com.universe.touchpoint.ai;

import android.util.Pair;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import java.util.List;

public interface ChoiceParser<CH> {

    Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(CH choices, AgentAction<?, ?> currentAction);

}
