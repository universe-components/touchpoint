package com.universe.touchpoint.memory.regions;

import android.content.ContentProvider;

import com.universe.touchpoint.TouchPointAction;
import com.universe.touchpoint.memory.TouchPointRegion;

import java.util.HashMap;

public class TransportRegion extends TouchPointRegion {

    private final HashMap<String, TouchPointAction> touchPointReceivers = new HashMap<>();
    private final HashMap<String, ContentProvider> touchPointProviders = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointAction receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointAction getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

    public HashMap<String, TouchPointAction> getTouchPointReceivers() {
        return touchPointReceivers;
    }

    public void putTouchPointProvider(String uri, ContentProvider provider) {
        touchPointProviders.put(uri, provider);
    }

    public ContentProvider getTouchPointProvider(String uri) {
        return touchPointProviders.get(uri);
    }

}
