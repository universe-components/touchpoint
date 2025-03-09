package com.universe.touchpoint.plan.executor;

import com.universe.touchpoint.TaskSocket;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.ai.AIModel;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.annotations.task.Task;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.meta.data.TaskMeta;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.utils.ClassUtils;

public class AgentActionExecutor<I, O> extends ActionExecutor<AgentAction<I, O>, O> {

    @Task("collect_metrics")
    @AIModel(name = Model.o1)
    private TaskMeta metricTaskMeta;

    @Override
    public void beforeRun(AgentAction<I, O> action) {
        String taskName = action.getContext().getTask();
        if (action.getMeta().getRole() == ActionRole.SUPERVISOR) {
            SupervisorFactory.getSupervisor(taskName).execute(action, taskName);
        }
    }

    @Override
    public O run(AgentAction<I, O> action) {
        String taskName = action.getContext().getTask();
        RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(taskName).getExecutor(action.getActionName());
        return tpReceiver.run((I) ClassUtils.getFirstParam(action.getInput()), action.getContext());
    }

    @Override
    public AgentAction<I, O> afterRun(AgentAction<I, O> action, O runResult) {
        action.setOutput(runResult);
        new TaskSocket(metricTaskMeta.getName()).send("I want to collect action and task metrics, where task metrics include the number of execution errors and prediction counts for multiple actions within the task, and action metrics include the prediction count for a single action.");
        return action;
    }

}
