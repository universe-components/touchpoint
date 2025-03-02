package com.universe.touchpoint.router;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RouteTable {

    private final Map<String, List<AgentActionMetaInfo>> predecessors = new ConcurrentHashMap<>();
    private final Map<String, List<AgentActionMetaInfo>> successors = new ConcurrentHashMap<>();

    private static RouteTable instance;
    private static final Object lock = new Object();

    public static RouteTable getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new RouteTable();
            }
            return instance;
        }
    }

    public void putPredecessors(String name, List<AgentActionMetaInfo> predecessors) {
        this.predecessors.put(name, predecessors);
    }

    public List<AgentActionMetaInfo> getPredecessors(String name) {
        return predecessors.get(name);
    }

    public void putSuccessors(String name, List<AgentActionMetaInfo> successors) {
        this.successors.put(name, successors);
    }

    public List<AgentActionMetaInfo> getSuccessors(String name) {
        return successors.get(name);
    }

    public boolean containsItem(String from, String to) {
        for (AgentActionMetaInfo toMeta : Objects.requireNonNull(successors.get(from))) {
            if (toMeta.getActionName().equals(to)) {
                return true;
            }
        }
        return false;
    }

}
