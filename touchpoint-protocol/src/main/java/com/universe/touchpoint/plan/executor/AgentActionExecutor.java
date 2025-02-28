package com.universe.touchpoint.plan.executor;

import android.content.Context;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import com.universe.touchpoint.utils.ClassUtils;

public class AgentActionExecutor<I extends TouchPoint, O> extends ActionExecutor<AgentAction<I, O>> {

    @Override
    public boolean beforeRun(AgentAction<I, O> action, Context context) {
        String taskName = action.getContext().getTask();
        int stateCode = action.getInput().getState().getCode();
        if (stateCode >= 300 && stateCode < 400) {
            CoordinatorFactory.getCoordinator(taskName).execute(action, taskName, context);
            return false;
        }
        if (stateCode >= 400) {
            SupervisorFactory.getSupervisor(taskName).execute(action, taskName, context);
            // If redirecting, rebuild the ActionGraph.
            new AgentSocketStateRouter<>().route(
                    null,
                    context,
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, action),
                    taskName);
        }
        return false;
    }

    @Override
    public <Rsp> Rsp run(AgentAction<I, O> action, Context context) {
        String taskName = action.getContext().getTask();
        RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(taskName).getExecutor(action.getActionName());
        return (Rsp) tpReceiver.run((I) ClassUtils.getFirstParam(action.getInput()), context);
    }

    @Override
    public <RunResult> AgentAction<I, O> afterRun(AgentAction<I, O> action, RunResult runResult, Context context) {
        String taskName = action.getContext().getTask();
        // If redirecting, rebuild the ActionGraph.
        new AgentSocketStateRouter<>().route(
                null,
                context,
                new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, action),
                taskName);
        action.setOutput((O) runResult);
        return action;
    }

}
