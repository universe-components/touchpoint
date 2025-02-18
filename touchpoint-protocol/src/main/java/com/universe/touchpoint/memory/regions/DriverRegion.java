package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.memory.TouchPointRegion;
import java.util.HashMap;
import java.util.List;

public class DriverRegion extends TouchPointRegion {

    private final HashMap<String, AgentActionMetaInfo> touchPointActions = new HashMap<>();
    private final HashMap<String, AgentActionMetaInfo> touchPointSwapActions = new HashMap<>();

    public void putTouchPointAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointActions.put(name, agentActionMetaInfo);
    }

    public boolean containsTouchPointAction(String name) {
        return touchPointActions.containsKey(name);
    }

    public AgentActionMetaInfo getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void putTouchPointSwapAction(String name, AgentActionMetaInfo agentActionMetaInfo) {
        touchPointSwapActions.put(name, agentActionMetaInfo);
    }

    public boolean containsTouchPointSwapAction(String name) {
        return touchPointSwapActions.containsKey(name);
    }

    public AgentActionMetaInfo getTouchPointSwapAction(String name) {
        return touchPointSwapActions.get(name);
    }

    public void clearTouchPointSwapActions() {
        touchPointSwapActions.clear();
    }

    public boolean containActions(List<ActionRole> roles) {
        for (AgentActionMetaInfo agentActionMetaInfo : touchPointActions.values()) {
            if (roles.contains(agentActionMetaInfo.getRole())) {
                return true;
            }
        }
        return false;
    }

}
