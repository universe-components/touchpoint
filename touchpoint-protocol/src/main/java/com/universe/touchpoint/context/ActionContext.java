package com.universe.touchpoint.context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.monitor.metric.ActionMetric;

import java.util.Map;

public class ActionContext {

    private String currentAction;
    private Map<String, ActionMetric> actionMetrics;

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public ActionMetric getActionMetric(String action) {
        return actionMetrics.computeIfAbsent(action, k -> new ActionMetric());
    }

    public AgentActionMetaInfo getActionMetaInfo(String action) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        return driverRegion.getTouchPointAction(action);
    }

}
