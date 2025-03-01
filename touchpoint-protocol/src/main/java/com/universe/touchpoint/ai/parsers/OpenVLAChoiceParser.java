package com.universe.touchpoint.ai.parsers;

import android.util.Pair;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.router.Router;
import java.util.ArrayList;
import java.util.List;

public class OpenVLAChoiceParser implements ChoiceParser<OpenVLA.ActionResponse> {

    @Override
    public Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(OpenVLA.ActionResponse choices, AgentAction<?, ?> currentAction) {
        List<AgentAction<?, ?>> agentActions = new ArrayList<>();

        AgentAction<OpenVLA.ActionResponse, ?> correctAction = (AgentAction<OpenVLA.ActionResponse, ?>) currentAction;
        List<AgentActionMetaInfo> nextActions = Router.route(correctAction, true);
        if (nextActions != null) {
            for (AgentActionMetaInfo nextAction : nextActions) {
                correctAction.setActionName(nextAction.getActionName());
                correctAction.setInput(choices);
                agentActions.add(correctAction);
            }
            return new Pair<>(agentActions, null);
        }
        return new Pair<>(null, new AgentFinish<>(choices, currentAction.getMeta()));
    }

}
