package com.universe.touchpoint.rolemodel.coordinator.handler;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, AgentActionMeta> {

    @Override
    public <C extends AgentContext> AgentActionMeta onStateChange(AgentAction<I, O> action, C agentContext, String task) {
        RoleExecutor<I, O> actionCoordinator = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(task).getExecutor(action.getInput().getState().getRedirectToAction());
        AgentAction<I, O> newAction = (AgentAction<I, O>) actionCoordinator.run(action.getInput(), action.getContext());
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        metaRegion.putTouchPointAction(newAction.getActionName(), newAction.getMeta());
        return newAction.getMeta();
    }

}
