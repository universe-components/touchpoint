package com.universe.touchpoint.plan;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import javax.annotation.Nullable;

public class ResultDispatcher {

    public static <R extends TouchPoint, F> String run(R result, @Nullable AgentActionMeta actionMeta) {
        assert actionMeta != null;
        String task = result.getContext().getTask();
        TouchPointChannel<?> channel = TouchPointChannelManager.selectChannel(actionMeta, task);
        F rs = (F) channel.send(result);
        if (rs instanceof String) {
            return (String) rs;
        }
        return null;
    }

}
