package com.universe.touchpoint;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.driver.ResultDispatcher;

public class Dispatcher {

    public static String dispatch(String content, String task) {
        StringBuilder resultBuilder = new StringBuilder();
        ActionGraphBuilder.getTaskGraph(task).getFirstNodes().forEach(
            actionMeta -> {
                AgentAction<?, ?> action = new AgentAction<>(actionMeta.getActionName(), actionMeta, new TouchPoint.Header(actionMeta));
                action.context.setTask(new TaskContext(content, task));
                String result = ResultDispatcher.run(action, actionMeta, Agent.getContext());
                resultBuilder.append(result).append("\n");
            }
        );
        return resultBuilder.toString();
    }

}