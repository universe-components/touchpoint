package com.universe.touchpoint.api.executor;

import com.universe.touchpoint.api.RoleExecutor;

import java.util.List;

public abstract class AgentActionExecutor<Req, Resp> implements RoleExecutor<Req, Resp> {

    protected List<String> internalParams;

    public List<String> getInternalParams() {
        return internalParams;
    }

}
