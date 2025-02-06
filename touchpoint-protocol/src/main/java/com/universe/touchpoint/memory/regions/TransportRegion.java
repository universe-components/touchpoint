package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.TouchPointAction;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;

public class TransportRegion extends TouchPointRegion {

    private final HashMap<String, TouchPointAction> touchPointReceivers = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointAction receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointAction getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

}
