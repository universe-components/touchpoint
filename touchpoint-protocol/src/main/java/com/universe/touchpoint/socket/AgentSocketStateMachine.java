package com.universe.touchpoint.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.annotations.transport.SocketProtocol;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.TouchPointConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public record AgentSocketStateMachine(AgentSocketProtocol socketProtocol) {

    private static final Object mLock = new Object();
    private static final Map<String, AgentSocketStateMachine> stateMachineMap = new HashMap<>();

    public static void registerInstance(String task, SocketProtocol protocol) {
        if (!stateMachineMap.containsKey(task)) {
            synchronized (mLock) {
                if (!stateMachineMap.containsKey(task)) {
                    stateMachineMap.put(task, new AgentSocketStateMachine(AgentSocketProtocolSelector.selectProtocol(protocol)));
                }
            }
        }
    }

    public static AgentSocketStateMachine getInstance(String task) {
        return stateMachineMap.get(task);
    }

    public void start(Context context, String task) {
        send(
                new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.TASK_READY),
                context,
                task
        );
    }

    public void send(AgentSocketStateContext<?> stateContext, Context context, String filterSuffix) {
        socketProtocol.send(stateContext, context, filterSuffix);
    }

    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context) {
        socketProtocol.registerReceiver(appContext, context);
    }

    public static class AgentSocketStateListener<C extends AgentContext> extends BroadcastReceiver {

        private final C context;

        public AgentSocketStateListener(C context) {
            this.context = context;
        }

        @Override
        public void onReceive(Context appContext, Intent intent) {
            byte[] stateContextBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_TASK_STATE_EVENT);
            if (stateContextBytes == null) {
                return;
            }
            new AgentSocketStateRouter<>().route(context, appContext, stateContextBytes, Objects.requireNonNull(intent.getAction()));
        }

    }

    public static class AgentSocketStateContext<C> {

        private final AgentSocketState socketState;
        private C context;

        public AgentSocketStateContext(AgentSocketState socketState) {
            this.socketState = socketState;
        }

        public AgentSocketStateContext(AgentSocketState socketState, C context) {
            this.socketState = socketState;
            this.context = context;
        }

        public AgentSocketState getSocketState() {
            return socketState;
        }

        public C getContext() {
            return context;
        }

    }

}
