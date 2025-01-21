package com.universe.touchpoint.arp;

import com.universe.touchpoint.AgentEntity;
import com.universe.touchpoint.TouchPoint;

public class AgentRouteItem {

    private String fromAgent;
    private AgentEntity toAgent;
    private Class<? extends TouchPoint> sharedClass;

    public String getFromAgent() {
        return fromAgent;
    }

    public void setFromAgent(String fromAgent) {
        this.fromAgent = fromAgent;
    }

    public AgentEntity getToAgent() {
        return toAgent;
    }

    public void setToAgent(AgentEntity toAgent) {
        this.toAgent = toAgent;
    }

    public Class<?> getSharedClass() {
        return sharedClass;
    }

    public void setSharedClass(Class<? extends TouchPoint> sharedClass) {
        this.sharedClass = sharedClass;
    }

}
