package com.universe.touchpoint.transport.broadcast;

import android.content.Context;
import android.content.IntentFilter;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionMeta;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.router.AgentRouter;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

public class TouchPointBroadcastReceiverRegistry implements TouchPointTransportRegistry {

    @Override
    public void register(Context context, AgentActionMeta agentActionMeta, String[] filters) {
        TouchPointBroadcastReceiver<? extends TouchPoint> tpReceiver = new TouchPointBroadcastReceiver<>(
                agentActionMeta.inputClass(), context);

        IntentFilter intentFilter = new IntentFilter();
        if (filters != null) {
            for (String filter : filters) {
                String filterAction = TouchPointHelper.touchPointFilterName(AgentRouter.buildChunk(
                        filter, Agent.getName()
                ));
                intentFilter.addAction(filterAction);
            }
        }
        context.registerReceiver(tpReceiver, intentFilter, Context.RECEIVER_EXPORTED);
    }

}
