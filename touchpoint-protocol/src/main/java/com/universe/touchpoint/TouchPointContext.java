package com.universe.touchpoint;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.monitor.TaskMetricManager;
import com.universe.touchpoint.monitor.metric.ActionMetric;

public record TouchPointContext(String task) {

    public ActionMetric getActionMetric(String actionName) {
        return TaskMetricManager.getActionMetric(task, actionName);
    }

    public AgentActionMetaInfo getActionMeta(String actionName) {
        DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
        return driverRegion.getTouchPointAction(actionName);
    }

}
