package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ResultDispatcher;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {

    public static <T, F> List<F> dispatch(String content, String task, T params, TouchPointContext context, TaskSocket.TaskCallbackListener callbackListener) {
        List<F> finalResult = new ArrayList<>();
        ActionGraphBuilder.getTaskGraph(task).getFirstNodes().forEach(
            actionMeta -> {
                AgentAction<T, ?> action = new AgentAction<>(actionMeta.getName(), actionMeta, new TouchPoint.Header(actionMeta), task);
                action.setContext(context);
                action.getContext().setTaskContext(new TaskContext(content));
                action.getHeader().setCallbackListener(callbackListener);
                if (params != null) {
                    action.setInput(params);
                }
                finalResult.add(ResultDispatcher.run(action, actionMeta));
            }
        );
        return finalResult;
    }

}