package com.universe.touchpoint.socket;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.rolemodel.coordinator.handler.ReorderActionReadyHandler;
import com.universe.touchpoint.rolemodel.coordinator.handler.SwitchActionReadyHandler;
import com.universe.touchpoint.socket.handler.RouterReadyHandler;
import com.universe.touchpoint.socket.handler.NegotiationConcludedHandler;
import com.universe.touchpoint.socket.handler.GlobalConfigReadyHandler;
import com.universe.touchpoint.socket.handler.RequestGraphReadyHandler;
import com.universe.touchpoint.socket.handler.ActionGraphReadyHandler;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Map;

public class AgentSocketStateRouter<C extends AgentContext> {

    private static final Map<AgentSocketState, AgentSocketStateHandler<?, ?>> stateHandlerMap = new HashMap<>();
    static {
        stateHandlerMap.put(AgentSocketState.ACTION_GRAPH_READY, new ActionGraphReadyHandler<>());
        stateHandlerMap.put(AgentSocketState.GLOBAL_CONFIG_READY, new GlobalConfigReadyHandler<>());
        stateHandlerMap.put(AgentSocketState.REQUEST_GRAPH_READY, new RequestGraphReadyHandler());
        stateHandlerMap.put(AgentSocketState.ROUTER_READY, new RouterReadyHandler());
        stateHandlerMap.put(AgentSocketState.NEGOTIATION_CONCLUDED, new NegotiationConcludedHandler());
        stateHandlerMap.put(AgentSocketState.COORDINATOR_ACTION_GRAPH_READY, new ReorderActionReadyHandler<>());
        stateHandlerMap.put(AgentSocketState.ACTION_READY, new SwitchActionReadyHandler<>());
    }

    public void route(C context, byte[] stateContextBytes, String filter) {
        AgentSocketStateMachine.AgentSocketStateContext<?> stateContext = SerializeUtils.deserializeFromByteArray(stateContextBytes, AgentSocketStateMachine.AgentSocketStateContext.class);
        route(context, stateContext, filter);
    }

    public <I, O> void route(C context, AgentSocketStateMachine.AgentSocketStateContext<I> stateContext, String filter) {
        try {
            AgentSocketStateHandler<I, O> stateHandler = (AgentSocketStateHandler<I, O>) stateHandlerMap.get(stateContext.getSocketState());
            assert stateHandler != null;
            AgentSocketState nextState = AgentSocketState.next(stateContext.getSocketState());
            if (nextState != null) {
                String scope = TouchPointHelper.extractFilter(filter);
                String socketFilter = TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, scope, nextState.getRole().name());
                String nextFilter = TouchPointHelper.touchPointFilterName(socketFilter);
                O output = stateHandler.onStateChange(stateContext.getContext(), context, scope);
                if (output != null) {
                    AgentSocketStateMachine.getInstance(context.getBelongTask()).send(
                            new AgentSocketStateMachine.AgentSocketStateContext<>(nextState, output), nextFilter);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
