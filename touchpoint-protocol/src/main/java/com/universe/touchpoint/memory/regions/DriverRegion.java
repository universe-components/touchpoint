package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;
import java.util.List;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();
    private final HashMap<String, List<ActionConfig>> predecessors = new HashMap<>();
    private final HashMap<String, List<ActionConfig>> successors = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public boolean containsTouchPointAction(String name) {
        return touchPointActions.containsKey(name);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void putPredecessors(String name, List<ActionConfig> predecessors) {
        this.predecessors.put(name, predecessors);
    }

    public List<ActionConfig> getPredecessors(String name) {
        return predecessors.get(name);
    }

    public void putSuccessors(String name, List<ActionConfig> successors) {
        this.successors.put(name, successors);
    }

    public List<ActionConfig> getSuccessors(String name) {
        return successors.get(name);
    }

}
