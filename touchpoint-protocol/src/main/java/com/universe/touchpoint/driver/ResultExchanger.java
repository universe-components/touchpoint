package com.universe.touchpoint.driver;

import android.content.Context;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.annotations.ActionRole;
import com.universe.touchpoint.annotations.SocketProtocol;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.driver.processor.AgentActionProcessor;
import com.universe.touchpoint.driver.processor.AgentFinishProcessor;
import com.universe.touchpoint.driver.processor.DefaultResultProcessor;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;

import org.apache.commons.lang3.tuple.Triple;

public class ResultExchanger {

    public static <R extends TouchPoint, T extends TouchPoint> String exchange(
            R result, String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transportType) {
        if (result.getHeader().getFromAction().role() == ActionRole.COORDINATOR) {
            Triple<TransportConfig<?>, AIModelConfig, SocketProtocol> globalConfig = new CoordinatorReadyHandler().onStateChange(result, null, context, task);
            AgentSocketStateMachine.getInstance().send(
                    new AgentSocketStateMachine.AgentSocketStateContext<>(
                            AgentSocketState.GLOBAL_CONFIG_DISTRIBUTED, globalConfig),
                    context,
                    task);
            return null;
        }

        ResultProcessor<?, ?> resultProcessor;
        if (result instanceof AgentAction && task != null) {
            resultProcessor = new AgentActionProcessor<>((AgentAction) result, goal, task, tpReceiver, context, transportType);
        } else if (result instanceof AgentFinish) {
            resultProcessor = new AgentFinishProcessor<>((AgentFinish) result, goal, task, tpReceiver, context, transportType);
        } else {
            resultProcessor = new DefaultResultProcessor(result, goal, null, tpReceiver, context, transportType);

        }

        return resultProcessor.process();
    }

}
