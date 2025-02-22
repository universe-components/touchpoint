package com.universe.touchpoint.router;

import android.content.Context;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class RedirectActionHandler<T extends TouchPoint> implements AgentSocketStateHandler<T, AgentActionMetaInfo> {

    @Override
    public <C extends AgentContext> AgentActionMetaInfo onStateChange(T action, C agentContext, Context context, String filterSuffix) {
        String task = action.getContext().getTask();
        String fromAction = ((AgentAction<?, ?>) action).getActionName();
        String toAction = action.getState().getRedirectToAction();
        if (action.getState().getRedirectToAction() != null && !RouteTable.getInstance().containsItem(fromAction, toAction)) {
            ((AgentAction<?, ?>) action).getMeta().getToActions().addToAction(task, toAction);
            return ((AgentAction<?, ?>) action).getMeta();
        }
        return null;
    }

}
