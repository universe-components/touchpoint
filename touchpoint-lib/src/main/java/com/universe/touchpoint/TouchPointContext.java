package com.universe.touchpoint;

import java.util.HashMap;

public class TouchPointContext {

    private final HashMap<String, TouchPointReceiver<?>> touchPointReceivers = new HashMap<>();

    public void putTouchPointReceiver(String name, TouchPointReceiver<?> receiver) {
        touchPointReceivers.put(name, receiver);
    }

    public TouchPointReceiver<?> getTouchPointReceiver(String name) {
        return touchPointReceivers.get(name);
    }

    public HashMap<String, TouchPointReceiver<?>> getTouchPointReceivers() {
        return touchPointReceivers;
    }

}
