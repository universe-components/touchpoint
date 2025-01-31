package com.universe.touchpoint.config;

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

}
