package com.universe.touchpoint.negotiation.handler;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.TransportConfig;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.negotiation.context.TaskActionContext;
import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.negotiation.AgentSocketStateHandler;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RouterReadyHandler implements AgentSocketStateHandler<ActionGraph, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(ActionGraph actionGraph, C actionContext, String task) {
        TaskActionContext taskActionContext = (TaskActionContext) actionContext;
        if (actionGraph != null) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            AgentActionMeta actionMetaInfo = metaRegion.getTouchPointAction(taskActionContext.getAction());
            List<AgentActionMeta> predecessors  = actionGraph.getPredecessors(actionMetaInfo);
            List<AgentActionMeta> successors = actionGraph.getSuccessors(actionMetaInfo);

            TransportConfig<?> transportConfig = ConfigManager.selectTransport(taskActionContext.getAction(), taskActionContext.getBelongTask());
            TouchPointTransportRegistry<?> registry = TouchPointTransportRegistryFactory.getRegistry(transportConfig.transportType());

            predecessors.forEach(action -> registry.register(actionMetaInfo, action.getName(), task, true));
            successors.forEach(action -> registry.register(actionMetaInfo, action.getName(), task, false));
            for (Map.Entry<AgentActionMeta, List<AgentActionMeta>> entry : actionGraph.getAdjList().entrySet()) {
                AgentActionMeta node = entry.getKey();
                List<AgentActionMeta> adjacentNodes = entry.getValue();
                metaRegion.putTouchPointAction(node.getName(), node);
                // Process adjacent nodes (neighbors)
                for (AgentActionMeta adjacentNode : adjacentNodes) {
                    metaRegion.putTouchPointAction(adjacentNode.getName(), adjacentNode);
                }
            }

            if (metaRegion.containActions(Collections.singletonList(ActionRole.COORDINATOR))) {
                ActionGraphBuilder.putGraph(task, actionGraph);
                AgentSocketConfig socketConfig = ConfigManager.selectAgentSocket(taskActionContext.getBelongTask());
                assert socketConfig != null;
                AgentSyncProtocolSelector.selectProtocol(socketConfig.getBindProtocol()).registerReceiver(
                        new TaskActionContext(taskActionContext.getAction(), task),
                        TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER, RoleType.MEMBER);
            }

            RouteTable.getInstance().putPredecessors(taskActionContext.getAction(), predecessors);
            RouteTable.getInstance().putSuccessors(taskActionContext.getAction(), successors);
        }
        return true;
    }

}
