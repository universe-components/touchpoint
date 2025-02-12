package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import java.util.List;
import java.util.Objects;

public class ActionGraphDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public <C extends AgentContext> Boolean onStateChange(Object input, C actionContext, Context context, String task) {
        ActionGraph actionGraph = (ActionGraph) input;
        TaskActionContext taskActionContext = (TaskActionContext) actionContext;
        if (actionGraph != null) {
            DriverRegion driverRegion = DriverRegion.getInstance(DriverRegion.class);
            AgentActionMetaInfo actionMetaInfo = driverRegion.getTouchPointAction(taskActionContext.getAction());
            List<AgentActionMetaInfo> predecessors  = actionGraph.getPredecessors(actionMetaInfo);
            List<AgentActionMetaInfo> successors = actionGraph.getSuccessors(actionMetaInfo);

            TouchPointTransportRegistry registry = TouchPointTransportRegistryFactory
                    .getRegistry(Objects.requireNonNull(Agent.agentConfig()).keySet().iterator().next());
            AgentActionManager manager = AgentActionManager.getInstance();

            predecessors.forEach(action -> registry.register(context, driverRegion.getTouchPointAction(action.actionName())));
            successors.forEach(action -> {
                try {
                    manager.registerAgentFinishReceiver(
                            context,
                            action.actionName(),
                            (Class<? extends TouchPoint>) Class.forName(driverRegion.getTouchPointAction(action.actionName()).inputClassName()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            if (driverRegion.containActions(ActionRole.COORDINATOR)) {
                ActionGraphBuilder.putGraph(task, actionGraph);
            }

            RouteTable.getInstance().putPredecessors(taskActionContext.getAction(), predecessors);
            RouteTable.getInstance().putSuccessors(taskActionContext.getAction(), successors);
        }
        return true;
    }

}
