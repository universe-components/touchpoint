package com.universe.touchpoint.socket;

import android.content.Context;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.handler.ActionGraphDistributedHandler;
import com.universe.touchpoint.socket.handler.ChannelEstablishedHandler;
import com.universe.touchpoint.socket.handler.GlobalConfigDistributedHandler;
import com.universe.touchpoint.socket.handler.GlobalConfigReadyHandler;
import com.universe.touchpoint.socket.handler.TaskParticipantReadyHandler;
import com.universe.touchpoint.socket.handler.TaskReadyHandler;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AgentSocketStateRouter<C extends AgentContext> {

    private static final Map<AgentSocketState, AgentSocketStateHandler<?, ?>> stateHandlerMap = new HashMap<>();
    static {
        stateHandlerMap.put(AgentSocketState.TASK_READY, new TaskReadyHandler());
        stateHandlerMap.put(AgentSocketState.PARTICIPANT_READY, new TaskParticipantReadyHandler());
        stateHandlerMap.put(AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED, new GlobalConfigDistributedHandler());
        stateHandlerMap.put(AgentSocketState.GLOBAL_CONFIG_READY, new GlobalConfigReadyHandler());
        stateHandlerMap.put(AgentSocketState.ACTION_GRAPH_DISTRIBUTED, new ActionGraphDistributedHandler());
        stateHandlerMap.put(AgentSocketState.CHANNEL_ESTABLISHED, new ChannelEstablishedHandler());
    }

    public <I, O> void route(C context, Context appContext, byte[] stateContextBytes, String filter) {
        try {
            AgentSocketStateMachine.AgentSocketStateContext<?> stateContext = SerializeUtils.deserializeFromByteArray(stateContextBytes, AgentSocketStateMachine.AgentSocketStateContext.class);
            AgentSocketStateHandler<I, O> stateHandler = (AgentSocketStateHandler<I, O>) stateHandlerMap.get(stateContext.getSocketState());
            assert stateHandler != null;
            AgentSocketState nextState = AgentSocketState.next(stateContext.getSocketState());
            if (nextState != null) {
                String filterSuffix = TouchPointHelper.extractSuffixFromFilter(Objects.requireNonNull(filter));
                AgentSocketStateMachine.getInstance(context.getBelongTask()).send(new AgentSocketStateMachine.AgentSocketStateContext<>(
                        nextState, stateHandler.onStateChange((I) stateContext.getContext(), context, appContext, filterSuffix)
                ), appContext, filterSuffix);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
