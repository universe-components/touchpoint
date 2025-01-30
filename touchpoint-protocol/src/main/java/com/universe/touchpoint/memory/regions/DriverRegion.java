package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();
    private final HashMap<String, List<ActionConfig>> touchPointTasks = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void addTouchPointTaskAction(ActionConfig action) {
        touchPointTasks.computeIfAbsent(Agent.getName(), k -> new ArrayList<>()).add(action);
    }

    public List<ActionConfig> getTouchPointTaskActions(String agentName) {
        return touchPointTasks.get(agentName);
    }

}
