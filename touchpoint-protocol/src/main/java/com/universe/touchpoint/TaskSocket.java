package com.universe.touchpoint;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.sync.AgentSyncProtocolSelector;
import java.util.List;

public class TaskSocket {

    protected final String task;

    public TaskSocket(String task) {
        this.task = task;
    }

    public <F> List<F> send(String goal) {
        return send(goal, null);
    }

    public <T, F> List<F> send(String goal, T params) {
        return send(goal, params, null);
    }

    public <T, F> List<F> send(String goal, T params, TouchPointContext context) {
        return send(goal, params, context, null);
    }

    public void send(String goal, TouchPointContext context, TaskCallbackListener callbackListener) {
        send(goal, null, context, callbackListener);
    }

    public <T extends TouchPoint> void send(T params) {
        if (params.getContext() != null) {
            AgentSyncProtocolSelector.selectProtocol(SocketProtocol.MQTT5).send(
                    params.getContext(),
                    TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER, task, RoleType.MEMBER.name()));
        }
    }

    public <T, F> List<F> send(String goal, T params, TouchPointContext context, TaskCallbackListener callbackListener) {
        if (context != null) {
            AgentSyncProtocolSelector.selectProtocol(SocketProtocol.MQTT5).send(
                    context,
                    TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER, task, RoleType.MEMBER.name()));
        }
        return Dispatcher.dispatch(goal, task, params, context, callbackListener);
    }

    public static abstract class TaskCallbackListener {

        public abstract <T> void onSuccess(T result);

    }

}
