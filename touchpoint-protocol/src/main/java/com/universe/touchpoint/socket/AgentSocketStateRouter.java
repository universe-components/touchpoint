package com.universe.touchpoint.socket;

import android.content.Context;

import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.router.RedirectActionHandler;
import com.universe.touchpoint.socket.handler.ActionGraphDistributedHandler;
import com.universe.touchpoint.socket.handler.ChannelEstablishedHandler;
import com.universe.touchpoint.socket.handler.GlobalConfigDistributedHandler;
import com.universe.touchpoint.socket.handler.GlobalConfigReadyHandler;
import com.universe.touchpoint.socket.handler.TaskParticipantReadyHandler;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentSocketStateRouter<C extends AgentContext> {

    private static final Map<AgentSocketState, AgentSocketStateHandler<?, ?>> stateHandlerMap = new HashMap<>();
    static {
        stateHandlerMap.put(AgentSocketState.PARTICIPANT_READY, new TaskParticipantReadyHandler<>());
        stateHandlerMap.put(AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED, new GlobalConfigDistributedHandler<>());
        stateHandlerMap.put(AgentSocketState.GLOBAL_CONFIG_READY, new GlobalConfigReadyHandler());
        stateHandlerMap.put(AgentSocketState.ACTION_GRAPH_DISTRIBUTED, new ActionGraphDistributedHandler());
        stateHandlerMap.put(AgentSocketState.CHANNEL_ESTABLISHED, new ChannelEstablishedHandler());
        stateHandlerMap.put(AgentSocketState.REDIRECT_ACTION_READY, new RedirectActionHandler<>());
    }

    public void route(C context, Context appContext, byte[] stateContextBytes, String filter) {
        AgentSocketStateMachine.AgentSocketStateContext<?> stateContext = SerializeUtils.deserializeFromByteArray(stateContextBytes, AgentSocketStateMachine.AgentSocketStateContext.class);
        route(context, appContext, stateContext, filter);
    }

    public <I, O> void route(C context, Context appContext, AgentSocketStateMachine.AgentSocketStateContext<I> stateContext, String filter) {
        try {
            AgentSocketStateHandler<I, O> stateHandler = (AgentSocketStateHandler<I, O>) stateHandlerMap.get(stateContext.getSocketState());
            assert stateHandler != null;
            AgentSocketState nextState = AgentSocketState.next(stateContext.getSocketState());
            if (nextState != null) {
                String filterSuffix = TouchPointHelper.extractSuffixFromFilter(Objects.requireNonNull(filter));
                O output = stateHandler.onStateChange(stateContext.getContext(), context, appContext, filterSuffix);
                if (output != null) {
                    AgentSocketStateMachine.getInstance(context.getBelongTask()).send(
                            new AgentSocketStateMachine.AgentSocketStateContext<>(nextState, output), appContext, filterSuffix);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
