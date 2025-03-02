package com.universe.touchpoint.plan.executor;

import android.content.Context;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.task.Task;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.utils.ClassUtils;

public class AgentActionExecutor<I extends TouchPoint, O> extends ActionExecutor<AgentAction<I, O>, O> {

    @Task("collect_metrics")
    private final TaskBuilder metricTaskBuilder;

    public AgentActionExecutor() {
        this.metricTaskBuilder = TaskBuilder.task("collect_metrics");
    }

    @Override
    public void beforeRun(AgentAction<I, O> action, Context context) {
        String taskName = action.getContext().getTask();
        CoordinatorFactory.getCoordinator(taskName).execute(action, taskName, context);
        if (action.getMeta().getRole() == ActionRole.SUPERVISOR) {
            SupervisorFactory.getSupervisor(taskName).execute(action, taskName, context);
        }
    }

    @Override
    public O run(AgentAction<I, O> action, Context context) {
        String taskName = action.getContext().getTask();
        RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(taskName).getExecutor(action.getActionName());
        return tpReceiver.run((I) ClassUtils.getFirstParam(action.getInput()), context);
    }

    @Override
    public AgentAction<I, O> afterRun(AgentAction<I, O> action, O runResult, Context context) {
        action.setOutput(runResult);
        metricTaskBuilder.run("I want to collect action and task metrics, where task metrics include the number of execution errors and prediction counts for multiple actions within the task, and action metrics include the prediction count for a single action.");
        return action;
    }

}
