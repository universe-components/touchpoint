package com.universe.touchpoint;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.selector.AgentSocketProtocolSelector;

public class TaskSocket {

    protected final String task;

    public TaskSocket(String task) {
        this.task = task;
    }

    public String send(String goal) {
        return send(goal, null, null);
    }

    public <T extends TouchPoint> String send(String goal, T params) {
        return send(goal, params, null);
    }

    public void send(String goal, TaskCallbackListener callbackListener) {
        send(goal, null, callbackListener);
    }

    public <T extends TouchPoint> String send(String goal, T params, TaskCallbackListener callbackListener) {
        if (params.getContext() != null) {
            AgentSocketProtocolSelector.selectProtocol(SocketProtocol.MQTT5).send(
                    params.getContext(),
                    TouchPointHelper.touchPointFilterName(TouchPointConstants.TOUCH_POINT_TASK_CONTEXT_FILTER, task, RoleType.ALL.name()));
        }
        return Dispatcher.dispatch(goal, task, params, callbackListener);
    }

    public static abstract class TaskCallbackListener {

        public abstract <T> void onSuccess(T result);

    }

}
