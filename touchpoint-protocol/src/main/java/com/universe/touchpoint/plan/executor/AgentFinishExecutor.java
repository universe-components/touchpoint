package com.universe.touchpoint.plan.executor;

import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.meta.AgentActionMeta;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.router.Router;
import java.util.List;

public class AgentFinishExecutor<O> extends ActionExecutor<AgentFinish<O>, O> {

    @Override
    public void beforeRun(AgentFinish<O> touchPoint) {
    }

    @Override
    public O run(AgentFinish<O> agentFinish) {
        String taskName = agentFinish.getContext().getTask();
        List<AgentActionMeta> predecessors = Router.route(agentFinish, false);
        if (predecessors == null) {
            TaskBuilder.TaskCallbackListener callbackListener = TaskBuilder.getBuilder(taskName).getCallbackListener();
            callbackListener.onSuccess(agentFinish);
        }
        return agentFinish.getOutput();
    }

    @Override
    public AgentFinish<O> afterRun(AgentFinish<O> agentFinish, O runResult) {
        return agentFinish;
    }

}
