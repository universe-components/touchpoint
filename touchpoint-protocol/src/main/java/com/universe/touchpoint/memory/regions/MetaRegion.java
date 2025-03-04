package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.agent.meta.AgentMeta;
import com.universe.touchpoint.agent.meta.TaskMeta;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.memory.TouchPointRegion;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaRegion extends TouchPointRegion {

    private final Map<String, AgentActionMeta> touchPointActions = new ConcurrentHashMap<>();
    private final Map<String, AgentActionMeta> touchPointSwapActions = new ConcurrentHashMap<>();
    private final Map<String, TaskMeta> touchPointTasks = new ConcurrentHashMap<>();
    private final Map<String, AgentMeta> touchPointAgents = new ConcurrentHashMap<>();

    public void putTouchPointAction(String name, AgentActionMeta agentActionMeta) {
        touchPointActions.put(name, agentActionMeta);
    }

    public boolean containsTouchPointAction(String name) {
        return touchPointActions.containsKey(name);
    }

    public AgentActionMeta getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void putTouchPointSwapAction(String name, AgentActionMeta agentActionMeta) {
        touchPointSwapActions.put(name, agentActionMeta);
    }

    public boolean containsTouchPointSwapAction(String name) {
        return touchPointSwapActions.containsKey(name);
    }

    public AgentActionMeta getTouchPointSwapAction(String name) {
        return touchPointSwapActions.get(name);
    }

    public void putTouchPointTask(String name, TaskMeta taskMeta) {
        touchPointTasks.put(name, taskMeta);
    }

    public boolean containsTouchPointTask(String name) {
        return touchPointTasks.containsKey(name);
    }

    public TaskMeta getTouchPointTask(String name) {
        return touchPointTasks.get(name);
    }

    public void putTouchPointAgent(String name, AgentMeta agentMeta) {
        touchPointAgents.put(name, agentMeta);
    }

    public boolean containsTouchPointAgent(String name) {
        return touchPointAgents.containsKey(name);
    }

    public AgentMeta getTouchPointAgent(String name) {
        return touchPointAgents.get(name);
    }

    public void clearTouchPointSwapActions() {
        touchPointSwapActions.clear();
    }

    public boolean containActions(List<ActionRole> roles) {
        for (AgentActionMeta agentActionMeta : touchPointActions.values()) {
            if (roles.contains(agentActionMeta.getRole())) {
                return true;
            }
        }
        return false;
    }

}
