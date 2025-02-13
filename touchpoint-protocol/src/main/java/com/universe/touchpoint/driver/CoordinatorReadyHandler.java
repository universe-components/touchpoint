package com.universe.touchpoint.driver;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.ActionCoordinator;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class CoordinatorReadyHandler implements AgentSocketStateHandler<Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(Object input, C agentContext, Context context, String task) {
        AgentAction action = (AgentAction) input;
        ActionCoordinator actionCoordinator = CollaborationFactory.getInstance(task).getOperator(action.getAction());
        ActionGraph actionGraph = actionCoordinator.run(action.getActionInput(), ActionGraphBuilder.getTaskGraph(task));
        ActionGraphBuilder.putGraph(task, actionGraph);
        return Pair.create(
                ConfigManager.selectTransport(action.getMeta(), task),
                ConfigManager.selectModel(null, action.getMeta(), task)
        );
    }

}
