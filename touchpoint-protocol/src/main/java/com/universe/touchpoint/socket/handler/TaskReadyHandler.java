package com.universe.touchpoint.socket.handler;

import android.content.Context;

import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.context.TaskActionContext;

public class TaskReadyHandler implements AgentSocketStateHandler<TaskActionContext, AgentActionMetaInfo> {

    @Override
    public <C extends AgentContext> AgentActionMetaInfo onStateChange(TaskActionContext actionCtx, C agentContext, Context context, String task) {
        try {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            return driverRegion.getTouchPointAction(actionCtx.getAction());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
