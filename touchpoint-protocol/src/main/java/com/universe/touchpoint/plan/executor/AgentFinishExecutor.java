package com.universe.touchpoint.plan.executor;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.router.Router;

import java.util.List;

public class AgentFinishExecutor extends ActionExecutor<AgentFinish> {

    @Override
    public boolean beforeRun(AgentFinish touchPoint, Context context) {
        return true;
    }

    @Override
    public <O> O run(AgentFinish finishParams, Context context) {
        String taskName = finishParams.getContext().getTask();
        List<AgentActionMetaInfo> predecessors = Router.route(finishParams, false);
        if (predecessors == null) {
            com.universe.touchpoint.api.executor.AgentFinishExecutor finishExecutor = (com.universe.touchpoint.api.executor.AgentFinishExecutor) TaskRoleExecutor.getInstance(taskName)
                    .getExecutor(finishParams.getHeader().getFromAction().getActionName());
            finishExecutor.run(finishParams, context);
        }
        return null;
    }

    @Override
    public <RunResult> void afterRun(AgentFinish touchPoint, RunResult runResult, Context context) {
    }

}
