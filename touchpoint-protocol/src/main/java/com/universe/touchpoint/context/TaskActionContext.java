package com.universe.touchpoint.context;

import java.util.List;

public class TaskActionContext extends AgentContext {

    private final String actionName;
    private List<String> routes;

    public TaskActionContext(String actionName, String taskName) {
        super(taskName);
        this.actionName = actionName;
    }

    public TaskActionContext(String actionName, String taskName, List<String> routes) {
        super(taskName);
        this.actionName = actionName;
        this.routes = routes;
    }

    public String getActionName() {
        return actionName;
    }

    public List<String> getRoutes() {
        return routes;
    }

}
