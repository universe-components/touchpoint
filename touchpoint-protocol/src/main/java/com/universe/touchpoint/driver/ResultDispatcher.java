package com.universe.touchpoint.driver;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;

public class ResultDispatcher {

    public static <R extends TouchPoint, T> String run(R result, AgentActionMetaInfo actionMeta, Context context) {
        TouchPointChannel channel = TouchPointChannelManager.selectChannel(actionMeta, context);
        T rs = (T) channel.send(result);
        if (rs instanceof String) {
            return (String) rs;
        }
        return null;
    }

}
