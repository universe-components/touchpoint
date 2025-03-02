package com.universe.touchpoint.plan.executor;

import android.content.Context;
import com.universe.touchpoint.TaskBuilder;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.router.Router;
import java.util.List;

public class AgentFinishExecutor<O> extends ActionExecutor<AgentFinish<O>, O> {

    @Override
    public void beforeRun(AgentFinish<O> touchPoint, Context context) {
    }

    @Override
    public O run(AgentFinish<O> agentFinish, Context context) {
        String taskName = agentFinish.getContext().getTask();
        List<AgentActionMetaInfo> predecessors = Router.route(agentFinish, false);
        if (predecessors == null) {
            TaskBuilder.TaskCallbackListener callbackListener = TaskBuilder.getBuilder(taskName).getCallbackListener();
            callbackListener.onSuccess(agentFinish, context);
        }
        return agentFinish.getOutput();
    }

    @Override
    public AgentFinish<O> afterRun(AgentFinish<O> agentFinish, O runResult, Context context) {
        return agentFinish;
    }

}
