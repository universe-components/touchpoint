package com.universe.touchpoint.ai;

import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;

import java.util.List;
import java.util.Map;

public interface ChoiceParser<C, CH> {

<ReqInput extends TouchPoint,
    ReqOutput extends TouchPoint,
    RespInput extends TouchPoint,
    RespOutput extends TouchPoint> Pair<List<AgentAction<RespInput, RespOutput>>, AgentFinish> parse(
            Map<C, List<CH>> choices, AgentAction<ReqInput, ReqOutput> currentAction);

}
