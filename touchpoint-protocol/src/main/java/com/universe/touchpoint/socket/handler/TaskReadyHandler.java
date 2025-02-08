package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.context.TaskActionContext;

public class TaskReadyHandler implements AgentSocketStateHandler<AgentActionMetaInfo> {

    @Override
    public <C extends AgentContext> AgentActionMetaInfo onStateChange(Object actionCtx, C agentContext, Context context, String task) {
        try {
            DriverRegion driverRegion = DriverRegion.getInstance(DriverRegion.class);
            return driverRegion.getTouchPointAction(((TaskActionContext) actionCtx).getAction());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
