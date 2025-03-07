package com.universe.touchpoint.plan;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import javax.annotation.Nullable;

public class ResultDispatcher {

    public static <R extends TouchPoint, F> F run(R result, @Nullable AgentActionMeta actionMeta) {
        assert actionMeta != null;
        String task = result.getContext().getTask();
        TouchPointChannel<?> channel = TouchPointChannelManager.selectChannel(actionMeta, task);
        return (F) channel.send(result);
    }

}
