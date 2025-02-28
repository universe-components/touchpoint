package com.universe.touchpoint.plan;

import android.content.Context;
import androidx.annotation.Nullable;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;

public class ResultDispatcher {

    public static <R extends TouchPoint, F> String run(R result, @Nullable AgentActionMetaInfo actionMeta, Context context) {
        assert actionMeta != null;
        String task = result.getContext().getTask();
        TouchPointChannel<?> channel = TouchPointChannelManager.selectChannel(actionMeta, task, context);
        F rs = (F) channel.send(result);
        if (rs instanceof String) {
            return (String) rs;
        }
        return null;
    }

}
