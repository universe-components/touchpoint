package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.memory.TouchPointRegion;
import java.util.HashMap;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public boolean containsTouchPointAction(String name) {
        return touchPointActions.containsKey(name);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public boolean containActions(ActionRole role) {
        for (AgentActionMetaInfo agentActionMetaInfo : touchPointActions.values()) {
            if (agentActionMetaInfo.role() == role) {
                return true;
            }
        }
        return false;
    }

}
