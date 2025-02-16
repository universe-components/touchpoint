package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;

public class TransportRegion extends TouchPointRegion {

    private final HashMap<String, TouchPointListener<?, ?>> touchPointReceivers = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointListener<?, ?> receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointListener<?, ?> getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

}
