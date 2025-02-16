package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.driver.processor.AgentActionProcessor;
import com.universe.touchpoint.driver.processor.DefaultResultProcessor;

public class ResultExchanger {

    public static <I extends TouchPoint, O extends TouchPoint, R extends TouchPoint> String exchange(
            R result, String goal, String task, Context context, Transport transportType) {
        int stateCode = ((AgentAction<I, O>) result).getActionInput().getState().getCode();
        if (stateCode >= 300 && stateCode < 400) {
            CoordinatorFactory.getCoordinator(task).execute((AgentAction<I, O>) result, task, context);
        } else if (stateCode >= 400) {
            SupervisorFactory.getSupervisor(task).execute((AgentAction<I, O>) result, task);
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
