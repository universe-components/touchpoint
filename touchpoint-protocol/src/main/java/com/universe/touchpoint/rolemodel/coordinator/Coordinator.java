package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.negotiation.AgentSocketState;
import com.universe.touchpoint.negotiation.AgentSocketStateMachine;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import java.util.Objects;

public class Coordinator {

    public void execute(TouchPointContext context) {
        String agentAction = context.getAction();
        AgentActionMeta actionMeta = ((MetaRegion) TouchPointMemory.getRegion(Region.META)).getTouchPointAction(agentAction);
        String prevGraphName = ActionGraphBuilder.getTaskGraph(context.getTask()).getName();
        String currGraphName = context.getTaskContext().getActionGraphContext().env();
        Model prevlangModel = actionMeta.getModel().getModel();
        Model prevVisionModel = actionMeta.getVisionModel().getModel();
        Model prevVisionLangModel = actionMeta.getVisionLangModel().getModel();
        Model currlangModel = context.getActionContext().getLangModel(agentAction);
        Model currVisionModel = context.getActionContext().getVisionModel(agentAction);
        Model currVisionLangModel = context.getActionContext().getVisionLangModel(agentAction);
        Transport prevTransport = actionMeta.getTransportConfig().transportType();
        Transport currTransport = context.getActionContext().getTransport(agentAction);

        if (!Objects.equals(prevGraphName, currGraphName)) {
            new AgentSocketStateRouter<>().route(
                    null,
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.ACTION_GRAPH_READY, agentAction),
                    context.getTask()
            );
        } else if (!Objects.equals(prevlangModel, currlangModel)
                || !Objects.equals(prevVisionModel, currVisionModel)
                || !Objects.equals(prevVisionLangModel, currVisionLangModel)
                || !Objects.equals(prevTransport, currTransport)) {
            new AgentSocketStateRouter<>().route(
                    null,
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.ACTION_READY, agentAction),
                    context.getTask()
            );
        }
    }

}
