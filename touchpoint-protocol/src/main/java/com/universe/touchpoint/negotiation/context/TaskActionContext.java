package com.universe.touchpoint.negotiation.context;

import com.universe.touchpoint.negotiation.AgentContext;

public class TaskActionContext extends AgentContext {

    private final String action;

    public TaskActionContext(String action, String task) {
        super(task);
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
