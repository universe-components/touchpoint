package com.universe.touchpoint.rolemodel.coordinator;

import android.content.Context;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.rolemodel.RoleScope;
import com.universe.touchpoint.rolemodel.coordinator.handler.ReorderActionReadyHandler;
import com.universe.touchpoint.rolemodel.coordinator.handler.SwitchActionModelReadyHandler;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.state.enums.TaskState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Coordinator<SocketInput, SocketOutput> {

    private final Map<Integer, AgentSocketStateHandler<SocketInput, SocketOutput>> handlerMap = new HashMap<>();
    private final Map<Integer, AgentSocketState> socketStateMap = new HashMap<>();
    private final Map<Integer, String> exceptionMap = new HashMap<>();

    public Coordinator() {
        handlerMap.put(TaskState.NEED_SWITCH_AI_MODEL.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionModelReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_AI_MODEL.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_AI_MODEL.getCode(), "Action[%s] has been switched model for task[%s]");

        handlerMap.put(TaskState.NEED_REORDER_ACTION.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new ReorderActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_REORDER_ACTION.getCode(), AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED);
        exceptionMap.put(TaskState.NEED_REORDER_ACTION.getCode(), "ActionGraph has been rebuild for task[%s]");
    }

    public void execute(AgentAction<?, ?> agentAction, String task, Context context) {
        int stateCode = agentAction.getActionInput().getState().getCode();
        SocketOutput result = Objects.requireNonNull(handlerMap.get(stateCode)).onStateChange((SocketInput) agentAction, null, context, task);
        AgentSocketStateMachine.getInstance(task).send(
                    new AgentSocketStateMachine.AgentSocketStateContext<>(socketStateMap.get(stateCode), result),
                    context,
                    task);
        String pattern = Objects.requireNonNull(exceptionMap.get(stateCode));
        if (Objects.requireNonNull(TaskState.getState(stateCode)).getScope() == RoleScope.ACTION_GRAPH) {
            throw new RuntimeException(String.format(pattern, task));
        } else {
            throw new RuntimeException(String.format(pattern, agentAction.getAction(), task));
        }
    }

}
