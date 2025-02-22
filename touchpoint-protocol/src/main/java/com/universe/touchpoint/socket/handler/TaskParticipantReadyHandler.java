package com.universe.touchpoint.socket.handler;

import android.content.Context;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class TaskParticipantReadyHandler implements AgentSocketStateHandler<AgentActionMetaInfo, ActionGraph> {

    @Override
    public <C extends AgentContext> ActionGraph onStateChange(AgentActionMetaInfo actionMeta, C agentContext, Context context, String task) {
        if (actionMeta != null) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.putTouchPointSwapAction(actionMeta.getActionName(), actionMeta);

            ActionGraphBuilder.getTaskGraph(task).addNode(actionMeta);
            actionMeta.getToActions().getToActions(task).forEach(toAction -> {
                if (driverRegion.containsTouchPointSwapAction(toAction)) {
                    ActionGraphBuilder.getTaskGraph(task).addEdge(actionMeta, driverRegion.getTouchPointSwapAction(toAction));
                }
            });
            return ActionGraphBuilder.getTaskGraph(task);
        }
        return null;
    }

}
