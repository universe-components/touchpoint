package com.universe.touchpoint.rolemodel.coordinator.handler;

import android.content.Context;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.operator.ActionOperator;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class SwitchActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, AgentAction<I, O>> {

    @Override
    public <C extends AgentContext> AgentAction<I, O> onStateChange(AgentAction<I, O> action, C agentContext, Context context, String task) {
        ActionOperator<I> actionCoordinator = (ActionOperator<I>) RoleExecutorFactory.getInstance(task).getExecutor(action.getInput().getState().getRedirectToAction());
        AgentAction<I, O> newAction = (AgentAction<I, O>) actionCoordinator.run(action.getInput(), action);

        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        driverRegion.putTouchPointAction(action.getAction(), newAction.getMeta());

        return newAction;
    }

}
