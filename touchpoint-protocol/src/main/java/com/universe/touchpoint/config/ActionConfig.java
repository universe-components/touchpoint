package com.universe.touchpoint.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ActionConfig {

    private String name;
    private String desc;
    private String[] toAgents;
    private String[] toActions;
    private String[] tasks;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getToActions() {
        return toActions;
    }

    public String[] getToAgents() {
        return toAgents;
    }

    public String[] getTasks() {
        return tasks;
    }

    public Set<String> getAllSuccessors() {
        Set<String> successors = new HashSet<>();
        if (toAgents != null) {
            successors.addAll(Arrays.asList(toAgents));
        }
        if (toActions != null) {
            successors.addAll(Arrays.asList(toActions));
        }
        return successors;
    }

}
