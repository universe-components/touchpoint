package com.universe.touchpoint.config.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void addToAction(String task, String action) {
        List<String> actions = toActions.get(task);
        if (actions == null) {
            toActions.put(task, new ArrayList<>());
        }
        assert actions != null;
        actions.add(action);
    }

    public Set<String> getTasks() {
        return toActions.keySet();
    }

    public void clearToAction(String task) {
        toActions.remove(task);
    }

}
