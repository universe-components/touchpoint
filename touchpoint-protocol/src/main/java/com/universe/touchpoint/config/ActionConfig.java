package com.universe.touchpoint.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ActionConfig {

    private String name;
    private String desc;
    private String[] fromAgents;
    private String[] fromActions;
    private String[] taskProposers;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getFromAgents() {
        return fromAgents;
    }

    public String[] getFromActions() {
        return fromActions;
    }

    public String[] getTaskProposers() {
        return taskProposers;
    }

    public Set<String> getAllPredecessors() {
        Set<String> predecessors = new HashSet<>();
        if (fromAgents != null) {
            predecessors.addAll(Arrays.asList(fromAgents));
        }
        if (fromActions != null) {
            predecessors.addAll(Arrays.asList(fromActions));
        }
        return predecessors;
    }

}
