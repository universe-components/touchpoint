package com.universe.touchpoint.driver;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.role.ActionRole;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.rolemodel.coordinator.ReorderActionReadyHandler;
import com.universe.touchpoint.driver.processor.AgentActionProcessor;
import com.universe.touchpoint.driver.processor.DefaultResultProcessor;
import com.universe.touchpoint.rolemodel.RoleExecutorFactory;
import com.universe.touchpoint.rolemodel.coordinator.SwitchActionModelReadyHandler;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.state.enums.TaskState;

public class ResultExchanger {

    public static <I extends TouchPoint, O extends TouchPoint, R extends TouchPoint, E> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        if (((AgentAction<I, O>) result).getMeta().role() == ActionRole.COORDINATOR) {
            if (((AgentAction<?, ?>) result).getActionInput().getState().getCode() == TaskState.NEED_REORDER_ACTION.getCode()) {
                Pair<TransportConfig<?>, AIModelConfig> globalConfig = new ReorderActionReadyHandler<I, O>().onStateChange((AgentAction<I, O>) result, null, context, task);
                AgentSocketStateMachine.getInstance(task).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(
                                AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED, globalConfig),
                        context,
                        task);
                throw new RuntimeException(String.format("ActionGraph has been rebuild for task[%s]", task));
            }
            if (((AgentAction<I, O>) result).getActionInput().getState().getCode() == TaskState.NEED_SWITCH_AI_MODEL.getCode()) {
                AgentAction<I, O> newAction = new SwitchActionModelReadyHandler<I, O>().onStateChange((AgentAction<I, O>) result, null, context, task);
                AgentSocketStateMachine.getInstance(task).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(
                                AgentSocketState.PARTICIPANT_READY, newAction.getMeta()),
                        context,
                        task);
                throw new RuntimeException(String.format("Action[%s] has been switched model for task[%s]", ((AgentAction<I, O>) result).getAction(), task));
            }
        }
        E actionSupervisor = (E) RoleExecutorFactory.getInstance(task).getExecutor(((AgentAction<I, O>) result).getAction());
        Object isRoute = RoleExecutorFactory.getInstance(task).run(result, actionSupervisor);
        if (isRoute instanceof Boolean && !(Boolean) isRoute) {
            throw new RuntimeException(String.format("ActionSupervisor run failed：%s is not passed", result));
        }

        ResultProcessor<?, ?> resultProcessor;
        if (task != null) {
            resultProcessor = new AgentActionProcessor<>((AgentAction<I, O>) result, goal, task, context, transportType);
        } else {
            resultProcessor = new DefaultResultProcessor<>(result, goal, null, context, transportType);

        }

        return resultProcessor.process();
    }

}
