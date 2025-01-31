package com.universe.touchpoint.config;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ActionConfig {

    private String name;
    private String desc;
    private String[] fromAgent;
    private String[] fromAction;
    private String[] taskProposers;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String[] getFromAgent() {
        return fromAgent;
    }

    public String[] getFromAction() {
        return fromAction;
    }

    public String[] getTaskProposers() {
        return taskProposers;
    }

    public Set<String> getAllPredecessors() {
        Set<String> predecessors = new HashSet<>();
        if (fromAgent != null) {
            predecessors.addAll(Arrays.asList(fromAgent));
        }
        if (fromAction != null) {
            predecessors.addAll(Arrays.asList(fromAction));
        }
        return predecessors;
    }

}
