package com.universe.touchpoint.context;

public abstract class AgentContext {

    protected String belongTask;

    public AgentContext(String taskName) {
        this.belongTask = taskName;
    }

    public String getBelongTask() {
        return belongTask;
    }

}
