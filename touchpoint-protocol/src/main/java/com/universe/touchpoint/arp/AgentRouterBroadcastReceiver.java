package com.universe.touchpoint.arp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.AgentEntity;
import com.universe.touchpoint.TouchPointConstants;

public class AgentRouterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String routeChunk = intent.getStringExtra(TouchPointConstants.TOUCH_POINT_ROUTER_EVENT_NAME);

        assert routeChunk != null;
        String[] routeChunks = AgentRouter.splitChunk(routeChunk);
        AgentRouter.addRoute(routeChunks[0], new AgentEntity(routeChunks[1]), null);
    }

}
