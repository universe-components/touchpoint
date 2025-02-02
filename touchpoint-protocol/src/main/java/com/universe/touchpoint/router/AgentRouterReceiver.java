package com.universe.touchpoint.router;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.AgentBroadcaster;
import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.agent.AgentEntity;
import com.universe.touchpoint.TouchPointConstants;

import java.util.ArrayList;

public class AgentRouterReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> routeChunks = intent.getStringArrayListExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME);

        assert routeChunks != null;
        for (String routeChunk : routeChunks) {
            String[] routeChunkPair = AgentRouter.splitChunk(routeChunk);
            try {
                AgentRouter.addRoute(routeChunkPair[0], new AgentEntity(routeChunkPair[1]));
                AgentBroadcaster.getInstance("transportConfig").send(
                        AgentBuilder.getBuilder().getConfig().getTransportConfig(), context);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
