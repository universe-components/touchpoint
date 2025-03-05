package com.universe.touchpoint.ai.parsers;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.router.Router;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class OpenVLAChoiceParser implements ChoiceParser<OpenVLA.ActionResponse> {

    @Override
    public Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(OpenVLA.ActionResponse choices, AgentAction<?, ?> currentAction) {
        List<AgentAction<?, ?>> agentActions = new ArrayList<>();

        AgentAction<OpenVLA.ActionResponse, ?> correctAction = (AgentAction<OpenVLA.ActionResponse, ?>) currentAction;
        List<AgentActionMeta> nextActions = Router.route(correctAction, true);
        if (nextActions != null) {
            for (AgentActionMeta nextAction : nextActions) {
                correctAction.setActionName(nextAction.getName());
                correctAction.setInput(choices);
                agentActions.add(correctAction);
            }
            return Pair.of(agentActions, null);
        }
        return Pair.of(null, new AgentFinish<>(choices, currentAction.getMeta()));
    }

}
