package com.universe.touchpoint.config.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionDependency {

    private final String action;
    private Map<String, List<String>> toActions = new HashMap<>();

    public ActionDependency(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setToActions(Map<String, List<String>> toActions) {
        this.toActions = toActions;
    }

    public List<String> getToActions(String task) {
        return toActions.get(task);
    }

}
