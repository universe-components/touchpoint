package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.context.TaskActionContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import java.util.List;
import java.util.Objects;

public class ActionGraphDistributedHandler implements AgentSocketStateHandler<Boolean> {

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public <C extends AgentContext> Boolean onStateChange(Object input, C actionContext, Context context, String filterSuffix) {
        ActionGraph actionGraph = (ActionGraph) input;
        TaskActionContext taskActionContext = (TaskActionContext) actionContext;
        if (actionGraph != null) {
            DriverRegion driverRegion = DriverRegion.getInstance(DriverRegion.class);
            List<ActionConfig> predecessors  = actionGraph.getPredecessors(new ActionConfig(taskActionContext.getActionName()));
            List<ActionConfig> successors = actionGraph.getSuccessors(new ActionConfig(taskActionContext.getActionName()));

            TouchPointTransportRegistry registry = TouchPointTransportRegistryFactory
                    .createRegistry(Objects.requireNonNull(Agent.agentConfig()).keySet().iterator().next());
            AgentActionManager manager = AgentActionManager.getInstance();

            predecessors.forEach(action -> registry.register(context, driverRegion.getTouchPointAction(action.getName())));
            successors.forEach(action -> manager.registerAgentFinishReceiver(
                    context, action.getName(), driverRegion.getTouchPointAction(action.getName()).inputClass()));

            driverRegion.putPredecessors(taskActionContext.getActionName(), predecessors);
            driverRegion.putSuccessors(taskActionContext.getActionName(), successors);
        }
        return true;
    }

}
