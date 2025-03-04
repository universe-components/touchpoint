package com.universe.touchpoint.transport;

public abstract class TouchPointRpcChannel<C> implements TouchPointChannel<String> {

    protected final C transportConfig;

    public TouchPointRpcChannel(C transportConfig) {
        this.transportConfig = transportConfig;
    }

}
