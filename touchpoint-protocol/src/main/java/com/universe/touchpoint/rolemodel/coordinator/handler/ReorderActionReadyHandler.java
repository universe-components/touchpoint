package com.universe.touchpoint.rolemodel.coordinator.handler;

import android.content.Context;

import com.universe.touchpoint.api.operator.ActionGraphOperator;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class ReorderActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, ActionGraph> {

    @Override
    public <C extends AgentContext> ActionGraph onStateChange(AgentAction<I, O> action, C agentContext, Context context, String task) {
        String coordinator = action.getInput().getContext().getTaskContext().getActionGraphContext().action();
        ActionGraphOperator<I> actionCoordinator = (ActionGraphOperator<I>) TaskRoleExecutor.getInstance(task).getExecutor(coordinator);
        return actionCoordinator.run(action.getInput(), context);
    }

}
