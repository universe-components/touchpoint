package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;
import java.util.List;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();
    private final HashMap<String, List<String>> predecessors = new HashMap<>();
    private final HashMap<String, List<String>> successors = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void putPredecessors(String name, List<String> predecessors) {
        this.predecessors.put(name, predecessors);
    }

    public List<String> getPredecessors(String name) {
        return predecessors.get(name);
    }

    public void putSuccessors(String name, List<String> successors) {
        this.successors.put(name, successors);
    }

    public List<String> getSuccessors(String name) {
        return successors.get(name);
    }

}
