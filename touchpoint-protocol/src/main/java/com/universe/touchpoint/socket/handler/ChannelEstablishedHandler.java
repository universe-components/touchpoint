package com.universe.touchpoint.socket.handler;

import android.util.Log;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class ChannelEstablishedHandler implements AgentSocketStateHandler<Boolean, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Boolean ready, C agentContext, String filterSuffix) {
        if (ready) {
            MetaRegion metaRegion = TouchPointMemory.getRegion(Region.META);
            metaRegion.clearTouchPointSwapActions();
            Log.i("ChannelEstablishedHandler", "Collaborative relationship established");
            return true;
        }
        return false;
    }

}
