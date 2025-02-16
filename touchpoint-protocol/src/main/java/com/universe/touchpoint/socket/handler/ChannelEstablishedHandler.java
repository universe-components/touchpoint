package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class ChannelEstablishedHandler implements AgentSocketStateHandler<Boolean, Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Boolean ready, C agentContext, Context context, String filterSuffix) {
        if (ready) {
            DriverRegion driverRegion = TouchPointMemory.getRegion(Region.DRIVER);
            driverRegion.clearTouchPointSwapActions();
            Log.i("ChannelEstablishedHandler", "Collaborative relationship established");
            return true;
        }
        return false;
    }

}
