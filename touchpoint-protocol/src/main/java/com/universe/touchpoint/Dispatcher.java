package com.universe.touchpoint;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.driver.ResultDispatcher;

public class Dispatcher {

    public static String dispatch(String content, String task) {
        StringBuilder resultBuilder = new StringBuilder();
        ActionGraphBuilder.getTaskGraph(task).getFirstNodes().forEach(
            actionMeta -> {
                AgentAction<?> action = new AgentAction<>(content, new TouchPoint.Header(actionMeta));
                String result = ResultDispatcher.run(action, actionMeta, Agent.getContext());
                resultBuilder.append(result).append("\n");
            }
        );
        return resultBuilder.toString();
    }

}