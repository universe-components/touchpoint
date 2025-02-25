package com.universe.touchpoint;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.params.ModalArguments;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.plan.ResultDispatcher;

import java.util.Arrays;

public class Dispatcher {

    public static String dispatch(String content, String task, Object... args) {
        StringBuilder resultBuilder = new StringBuilder();
        ActionGraphBuilder.getTaskGraph(task).getFirstNodes().forEach(
            actionMeta -> {
                AgentAction<ModalArguments, ?> action = new AgentAction<>(actionMeta.getActionName(), actionMeta, new TouchPoint.Header(actionMeta), task);
                action.getContext().setTaskContext(new TaskContext(content));
                if (args != null) {
                    action.setInput(new ModalArguments(Arrays.asList(args)));
                }
                String result = ResultDispatcher.run(action, actionMeta, Agent.getContext());
                resultBuilder.append(result).append("\n");
            }
        );
        return resultBuilder.toString();
    }

}