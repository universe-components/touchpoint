package com.universe.touchpoint.driver.coordinator;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.ActionCoordinator;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.driver.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class CoordinatorReadyHandler<I extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I>, Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(AgentAction<I> action, C agentContext, Context context, String task) {
        ActionCoordinator<I> actionCoordinator = (ActionCoordinator<I>) RoleExecutorFactory.getInstance(task).getOperator(action.getAction());
        ActionGraph actionGraph = actionCoordinator.run(action.getActionInput(), ActionGraphBuilder.getTaskGraph(task));
        ActionGraphBuilder.putGraph(task, actionGraph);
        return Pair.create(
                ConfigManager.selectTransport(action.getMeta(), task),
                ConfigManager.selectModel(null, action.getMeta(), task)
        );
    }

}
