package com.universe.touchpoint.plan.executor;

import android.content.Context;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.router.Router;
import java.util.List;

public class AgentFinishExecutor extends ActionExecutor<AgentFinish> {

    @Override
    public boolean beforeRun(AgentFinish touchPoint, Context context) {
        return true;
    }

    @Override
    public <O> O run(AgentFinish agentFinish, Context context) {
        String taskName = agentFinish.getContext().getTask();
        List<AgentActionMetaInfo> predecessors = Router.route(agentFinish, false);
        if (predecessors == null) {
            TaskBuilder.TaskCallbackListener callbackListener = TaskBuilder.getBuilder(taskName).getCallbackListener();
            callbackListener.onSuccess(agentFinish, context);
        }
        return (O) agentFinish.getOutput();
    }

    @Override
    public <RunResult> AgentFinish afterRun(AgentFinish agentFinish, RunResult runResult, Context context) {
        return agentFinish;
    }

}
