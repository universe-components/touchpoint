package com.universe.touchpoint.transport.broadcast;

import android.content.Context;
import android.content.IntentFilter;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

public class TouchPointBroadcastReceiverRegistry implements TouchPointTransportRegistry<Object> {

    @Override
    public void register(Context context, AgentActionMetaInfo agentActionMetaInfo, String previousAction, String task) {
        TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver;
        try {
            tpReceiver = new TouchPointBroadcastReceiver<>(AgentAction.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IntentFilter intentFilter = new IntentFilter(TouchPointHelper.touchPointFilterName(previousAction));
        context.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);

        TouchPointChannelManager.registerContextReceiver(agentActionMetaInfo.getActionName(), agentActionMetaInfo.getClassName(), task);
    }

    @Override
    public void init(Context context, Object transportConfig) {
    }

}
