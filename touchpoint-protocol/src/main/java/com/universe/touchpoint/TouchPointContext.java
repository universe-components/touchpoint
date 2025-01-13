package com.universe.touchpoint;

import java.util.HashMap;

public class TouchPointContext {

    private final HashMap<String, TouchPointListener<?>> touchPointReceivers = new HashMap<>();

    public void putTouchPointReceiver(String name, TouchPointListener<?> receiver) {
        touchPointReceivers.put(name, receiver);
    }

    public TouchPointListener<?> getTouchPointReceiver(String name) {
        return touchPointReceivers.get(name);
    }

    public HashMap<String, TouchPointListener<?>> getTouchPointReceivers() {
        return touchPointReceivers;
    }

}
