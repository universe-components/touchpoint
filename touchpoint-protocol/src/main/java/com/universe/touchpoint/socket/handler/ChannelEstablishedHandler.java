package com.universe.touchpoint.socket.handler;

import android.content.Context;
import android.util.Log;

import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.socket.AgentSocketStateHandler;

public class ChannelEstablishedHandler implements AgentSocketStateHandler<Boolean> {

    @Override
    public <C extends AgentContext> Boolean onStateChange(Object ready, C agentContext, Context context, String filterSuffix) {
        if ((Boolean) ready) {
            Log.i("ChannelEstablishedHandler", "channel established");
            return true;
        }
        return false;
    }

}
