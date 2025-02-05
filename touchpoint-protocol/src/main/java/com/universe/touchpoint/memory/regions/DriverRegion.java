package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

}
