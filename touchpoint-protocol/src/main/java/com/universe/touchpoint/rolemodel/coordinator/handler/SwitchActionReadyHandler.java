package com.universe.touchpoint.rolemodel.coordinator.handler;

import com.universe.touchpoint.meta.AgentActionMeta;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, AgentActionMeta> {

    @Override
    public <C extends AgentContext> AgentActionMeta onStateChange(AgentAction<I, O> action, C agentContext, String task) {
        RoleExecutor<I, O> actionCoordinator = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(task).getExecutor(action.getInput().getState().getRedirectToAction());
        AgentAction<I, O> newAction = (AgentAction<I, O>) actionCoordinator.run(action.getInput());
        MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
        metaRegion.putTouchPointAction(newAction.getActionName(), newAction.getMeta());
        return newAction.getMeta();
    }

}
