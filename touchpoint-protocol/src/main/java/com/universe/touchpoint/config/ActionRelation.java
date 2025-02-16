package com.universe.touchpoint.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionRelation {

    private final String action;
    private Map<String, List<String>> toActions = new HashMap<>();

    public ActionRelation(String action) {
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
