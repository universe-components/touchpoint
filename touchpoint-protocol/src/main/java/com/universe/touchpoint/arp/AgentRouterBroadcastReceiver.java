package com.universe.touchpoint.arp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.AgentEntity;
import com.universe.touchpoint.TouchPointConstants;

import java.util.ArrayList;

public class AgentRouterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ArrayList<String> routeChunks = intent.getStringArrayListExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME);

        assert routeChunks != null;
        for (String routeChunk : routeChunks) {
            String[] routeChunkPair = AgentRouter.splitChunk(routeChunk);
            AgentRouter.addRoute(routeChunkPair[0], new AgentEntity(routeChunkPair[1]), null);
        }
    }

}
