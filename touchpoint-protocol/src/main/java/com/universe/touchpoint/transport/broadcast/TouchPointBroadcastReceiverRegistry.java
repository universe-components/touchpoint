package com.universe.touchpoint.transport.broadcast;

import android.content.Context;
import android.content.IntentFilter;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

public class TouchPointBroadcastReceiverRegistry implements TouchPointTransportRegistry {

    @Override
    public void register(Context context, AgentActionMetaInfo agentActionMetaInfo) {
        TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver;
        try {
            tpReceiver = new TouchPointBroadcastReceiver<>(
                    Class.forName(agentActionMetaInfo.inputClassName()), context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        IntentFilter intentFilter = new IntentFilter(TouchPointHelper.touchPointFilterName(agentActionMetaInfo.actionName()));
        context.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);

        TouchPointChannelManager.registerContextReceiver(agentActionMetaInfo.actionName(), agentActionMetaInfo.className());
    }

    @Override
    public void init(Context context, Object transportConfig) {
    }

}
