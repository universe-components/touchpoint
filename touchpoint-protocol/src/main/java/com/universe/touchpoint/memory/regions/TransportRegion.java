package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.api.TouchPointExecutor;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;

public class TransportRegion extends TouchPointRegion {

    private final HashMap<String, TouchPointExecutor<?, ?>> touchPointReceivers = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointExecutor<?, ?> receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointExecutor<?, ?> getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

}
