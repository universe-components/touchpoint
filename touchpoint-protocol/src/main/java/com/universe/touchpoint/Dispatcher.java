package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ResultDispatcher;

public class Dispatcher {

    public static <T extends TouchPoint> String dispatch(String content, String task, T params, TaskSocket.TaskCallbackListener callbackListener) {
        StringBuilder resultBuilder = new StringBuilder();
        ActionGraphBuilder.getTaskGraph(task).getFirstNodes().forEach(
            actionMeta -> {
                AgentAction<T, ?> action = new AgentAction<>(actionMeta.getName(), actionMeta, new TouchPoint.Header(actionMeta), task);
                action.getContext().setTaskContext(new TaskContext(content));
                action.getHeader().setCallbackListener(callbackListener);
                if (params != null) {
                    action.setInput(params);
                }
                String result = ResultDispatcher.run(action, actionMeta);
                resultBuilder.append(result).append("\n");
            }
        );
        return resultBuilder.toString();
    }

}