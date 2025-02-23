package com.universe.touchpoint.rolemodel.coordinator;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.context.TouchPointState;
import com.universe.touchpoint.rolemodel.RoleScope;
import com.universe.touchpoint.rolemodel.coordinator.handler.ReorderActionReadyHandler;
import com.universe.touchpoint.rolemodel.coordinator.handler.SwitchActionReadyHandler;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateHandler;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.context.state.enums.TaskState;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Coordinator<SocketInput, SocketOutput> {

    private final Map<Integer, AgentSocketStateHandler<SocketInput, SocketOutput>> handlerMap = new HashMap<>();
    private final Map<Integer, AgentSocketState> socketStateMap = new HashMap<>();
    private final Map<Integer, String> exceptionMap = new HashMap<>();
    {
        handlerMap.put(TaskState.NEED_SWITCH_LANG_MODEL.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_LANG_MODEL.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_LANG_MODEL.getCode(), "Action[%s] has been switched language model for task[%s]");

        handlerMap.put(TaskState.NEED_SWITCH_VISION_MODEL.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_VISION_MODEL.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_VISION_MODEL.getCode(), "Action[%s] has been switched vision model for task[%s]");

        handlerMap.put(TaskState.NEED_SWITCH_VISION_LANG_MODEL.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_VISION_LANG_MODEL.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_VISION_LANG_MODEL.getCode(), "Action[%s] has been switched vision lang model for task[%s]");

        handlerMap.put(TaskState.NEED_SWITCH_TRANSPORT.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_TRANSPORT.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_TRANSPORT.getCode(), "Action[%s] has been switched transport for task[%s]");

        handlerMap.put(TaskState.NEED_SWITCH_ACTION.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new SwitchActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_SWITCH_ACTION.getCode(), AgentSocketState.PARTICIPANT_READY);
        exceptionMap.put(TaskState.NEED_SWITCH_ACTION.getCode(), "Action[%s] has been changed for task[%s]");

        handlerMap.put(TaskState.NEED_REORDER_ACTION.getCode(), (AgentSocketStateHandler<SocketInput, SocketOutput>) new ReorderActionReadyHandler<>());
        socketStateMap.put(TaskState.NEED_REORDER_ACTION.getCode(), AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED);
        exceptionMap.put(TaskState.NEED_REORDER_ACTION.getCode(), "ActionGraph has been rebuild for task[%s]");
    }

    public void execute(AgentAction<?, ?> agentAction, String task, Context context) {
        int stateCode = agentAction.getInput().getState().getCode();
        SocketOutput result = Objects.requireNonNull(handlerMap.get(stateCode)).onStateChange((SocketInput) agentAction, null, context, task);
        AgentSocketStateMachine.getInstance(task).send(
                    new AgentSocketStateMachine.AgentSocketStateContext<>(socketStateMap.get(stateCode), result),
                    context,
                    task);
        String pattern = Objects.requireNonNull(exceptionMap.get(stateCode));
        if (Objects.requireNonNull(TaskState.getState(stateCode)).getScope() == RoleScope.ACTION_GRAPH) {
            Log.i("Coordinator", String.format(pattern, task));
            agentAction.setState(new TouchPointState(
                    Objects.requireNonNull(socketStateMap.get(stateCode)).getCode(), String.format(pattern, task)));
        } else {
            Log.i("Coordinator", String.format(pattern, agentAction.getActionName(), task));
            agentAction.setState(new TouchPointState(
                    Objects.requireNonNull(socketStateMap.get(stateCode)).getCode(), String.format(pattern, agentAction.getActionName(), task)));
        }
    }

}
