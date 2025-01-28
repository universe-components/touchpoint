package com.universe.touchpoint.transport;

import android.content.Context;

public abstract class TouchPointRpcChannel<C> extends TouchPointChannel {

    protected final C transportConfig;

    public TouchPointRpcChannel(Context context, C transportConfig) {
        super(context);
        this.transportConfig = transportConfig;
    }

}
