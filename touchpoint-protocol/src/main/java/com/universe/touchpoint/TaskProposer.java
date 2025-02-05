package com.universe.touchpoint;

import android.content.Context;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.utils.AnnotationUtils;

public class TaskProposer {

    public static void init(Context context) {
        if (Agent.isAnnotationPresent(com.universe.touchpoint.annotations.TaskProposer.class)) {
            TransportConfig<?> transportConfigWrapper;
            try {
                transportConfigWrapper = (TransportConfig<?>) AnnotationUtils.annotation2Config(
                        Agent.getApplicationClass(), TransportConfigMapping.annotation2Config);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            AgentBuilder.getBuilder().getConfig().setTransportConfig(transportConfigWrapper);

            for (String task : Agent.getTasks()) {
                AgentSocketStateMachine.getInstance().registerReceiver(context, new TaskContext(task));
                AgentSocketStateMachine.getInstance().send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.TASK_READY),
                        context,
                        task);
            }
        }
    }

}
