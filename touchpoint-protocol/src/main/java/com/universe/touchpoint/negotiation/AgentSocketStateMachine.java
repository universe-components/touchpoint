package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public record AgentSocketStateMachine(AgentSyncProtocol socketProtocol) {

    private static final Object mLock = new Object();
    private static final Map<String, AgentSocketStateMachine> stateMachineMap = new HashMap<>();

    public static void registerInstance(String task, SocketProtocol protocol) {
        if (!stateMachineMap.containsKey(task)) {
            synchronized (mLock) {
                if (!stateMachineMap.containsKey(task)) {
                    stateMachineMap.put(task, new AgentSocketStateMachine(AgentSyncProtocolSelector.selectProtocol(protocol)));
                }
            }
        }
    }

    public static AgentSocketStateMachine getInstance(String task) {
        return stateMachineMap.get(task);
    }

    public void send(AgentSocketStateContext<?> stateContext, String filter) {
        socketProtocol.send(stateContext, TouchPointHelper.touchPointFilterName(filter));
    }

    public <C extends AgentContext> void registerReceiver(@Nullable C context, RoleType role) {
        socketProtocol.registerReceiver(context, TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER, role);
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
