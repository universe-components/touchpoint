package com.universe.touchpoint.router;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;

import java.util.Collections;
import java.util.List;

public class Router {

    public static<F extends TouchPoint> List<AgentActionMeta> route(F from, boolean isCalling) {
        String actionName = null;
        if (from instanceof AgentAction<?, ?>) {
            actionName = ((AgentAction<?, ?>) from).getActionName();
        } else if (from instanceof AgentFinish) {
            actionName = from.getHeader().getFromAction().getName();
        }
        if (from.getState().getRedirectToAction() != null) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            return Collections.singletonList(metaRegion.getTouchPointAction(from.getState().getRedirectToAction()));
        }
        if (isCalling) {
            return RouteTable.getInstance().getSuccessors(actionName);
        }
        return RouteTable.getInstance().getPredecessors(actionName);
    }

}
