package com.universe.touchpoint.plan;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.plan.executor.AgentActionExecutor;
import com.universe.touchpoint.plan.executor.AgentFinishExecutor;
import com.universe.touchpoint.plan.executor.DefaultActionExecutor;

public class ActionExecutionSelector {

    public static ActionExecutor<?, ?> getExecutor(TouchPoint touchPoint) {
        if (touchPoint instanceof AgentAction<?, ?>) {
            return new AgentActionExecutor<>();
        }
        if (touchPoint instanceof AgentFinish) {
            return new AgentFinishExecutor<>();
        }
        return new DefaultActionExecutor<>();
    }

}
