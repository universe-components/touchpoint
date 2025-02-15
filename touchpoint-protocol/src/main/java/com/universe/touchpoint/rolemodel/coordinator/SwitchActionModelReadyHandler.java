package com.universe.touchpoint.rolemodel.coordinator;

import android.content.Context;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.operator.ActionOperator;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class SwitchActionModelReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, AgentAction<I, O>> {

    @Override
    public <C extends AgentContext> AgentAction<I, O> onStateChange(AgentAction<I, O> action, C agentContext, Context context, String task) {
        ActionOperator<I> actionCoordinator = (ActionOperator<I>) RoleExecutorFactory.getInstance(task).getExecutor(action.getAction());
        return (AgentAction<I, O>) actionCoordinator.run(action.getActionInput(), action);
    }

}
