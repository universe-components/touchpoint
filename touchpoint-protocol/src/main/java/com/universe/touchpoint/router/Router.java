package com.universe.touchpoint.router;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.context.TouchPoint;
import java.util.List;

public class Router {

    public static<F extends TouchPoint> List<AgentActionMetaInfo> route(F from, boolean isCalling) {
        String actionName = null;
        if (from instanceof AgentAction<?, ?>) {
            actionName = ((AgentAction<?, ?>) from).getActionName();
        } else if (from instanceof AgentFinish) {
            actionName = from.getHeader().getFromAction().getActionName();
        }
        if (isCalling) {
            return RouteTable.getInstance().getSuccessors(actionName);
        }
        return RouteTable.getInstance().getPredecessors(actionName);
    }

}
