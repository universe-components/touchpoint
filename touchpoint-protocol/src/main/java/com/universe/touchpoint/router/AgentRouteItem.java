package com.universe.touchpoint.router;

import com.universe.touchpoint.TouchPoint;

public class AgentRouteItem {

    private String fromAgent;
    private String toAgent;
    private Class<? extends TouchPoint> sharedClass;

    public String getFromAgent() {
        return fromAgent;
    }

    public void setFromAgent(String fromAgent) {
        this.fromAgent = fromAgent;
    }

    public String getToAgent() {
        return toAgent;
    }

    public void setToAgent(String toAgent) {
        this.toAgent = toAgent;
    }

    public Class<?> getSharedClass() {
        return sharedClass;
    }

    public void setSharedClass(Class<? extends TouchPoint> sharedClass) {
        this.sharedClass = sharedClass;
    }

}
