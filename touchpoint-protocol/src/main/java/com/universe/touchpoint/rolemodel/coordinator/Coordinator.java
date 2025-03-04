package com.universe.touchpoint.rolemodel.coordinator;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.ai.Model;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import java.util.Objects;

public class Coordinator {

    public void execute(AgentAction<?, ?> agentAction, String task) {
        String prevGraphName = ActionGraphBuilder.getTaskGraph(task).getName();
        String currGraphName = agentAction.getContext().getTaskContext().getActionGraphContext().env();
        Model prevlangModel = agentAction.getMeta().getModel().getModel();
        Model prevVisionModel = agentAction.getMeta().getVisionModel().getModel();
        Model prevVisionLangModel = agentAction.getMeta().getVisionLangModel().getModel();
        Model currlangModel = agentAction.getContext().getActionContext().getLangModel(agentAction.getActionName());
        Model currVisionModel = agentAction.getContext().getActionContext().getVisionModel(agentAction.getActionName());
        Model currVisionLangModel = agentAction.getContext().getActionContext().getVisionLangModel(agentAction.getActionName());
        Transport prevTransport = agentAction.getMeta().getTransportConfig().transportType();
        Transport currTransport = agentAction.getContext().getActionContext().getTransport(agentAction.getActionName());

        if (!Objects.equals(prevGraphName, currGraphName)) {
            new AgentSocketStateRouter<>().route(
                    null,
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.ACTION_GRAPH_READY, agentAction),
                    task
            );
        } else if (!Objects.equals(prevlangModel, currlangModel)
                || !Objects.equals(prevVisionModel, currVisionModel)
                || !Objects.equals(prevVisionLangModel, currVisionLangModel)
                || !Objects.equals(prevTransport, currTransport)) {
            new AgentSocketStateRouter<>().route(
                    null,
                    new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.ACTION_READY, agentAction),
                    task
            );
        }
    }

}
