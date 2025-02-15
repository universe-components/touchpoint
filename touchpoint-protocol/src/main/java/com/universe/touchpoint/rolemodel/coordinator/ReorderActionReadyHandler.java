package com.universe.touchpoint.rolemodel.coordinator;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.api.operator.ActionGraphOperator;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.driver.ActionGraph;
import com.universe.touchpoint.driver.ActionGraphBuilder;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class ReorderActionReadyHandler<I extends TouchPoint, O extends TouchPoint> implements AgentSocketStateHandler<AgentAction<I, O>, Pair<TransportConfig<?>, AIModelConfig>> {

    @Override
    public <C extends AgentContext> Pair<TransportConfig<?>, AIModelConfig> onStateChange(AgentAction<I, O> action, C agentContext, Context context, String task) {
        ActionGraphOperator<I> actionCoordinator = (ActionGraphOperator<I>) RoleExecutorFactory.getInstance(task).getExecutor(action.getAction());
        ActionGraph actionGraph = actionCoordinator.run(action.getActionInput(), ActionGraphBuilder.getTaskGraph(task));
        ActionGraphBuilder.putGraph(task, actionGraph);
        return Pair.create(
                ConfigManager.selectTransport(action.getMeta(), task),
                ConfigManager.selectModel(null, action.getMeta(), task)
        );
    }

}
