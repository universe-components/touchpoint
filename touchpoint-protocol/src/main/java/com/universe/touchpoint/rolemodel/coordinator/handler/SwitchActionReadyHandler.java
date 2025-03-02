package com.universe.touchpoint.rolemodel.coordinator.handler;

import android.content.Context;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, AgentActionMetaInfo> {

    @Override
    public <C extends AgentContext> AgentActionMetaInfo onStateChange(AgentAction<I, O> action, C agentContext, Context context, String task) {
        RoleExecutor<I, O> actionCoordinator = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(task).getExecutor(action.getInput().getState().getRedirectToAction());
        AgentAction<I, O> newAction = (AgentAction<I, O>) actionCoordinator.run(action.getInput(), context);
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        driverRegion.putTouchPointAction(newAction.getActionName(), newAction.getMeta());
        return newAction.getMeta();
    }

}
