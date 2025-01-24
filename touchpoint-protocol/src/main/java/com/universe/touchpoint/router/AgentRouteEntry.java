package com.universe.touchpoint.router;

import com.universe.touchpoint.AgentEntity;

public class AgentRouteEntry {

    private String fromAgent;
    private AgentEntity toAgent;

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

}
