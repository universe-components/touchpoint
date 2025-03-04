package com.universe.touchpoint.memory;

import com.universe.touchpoint.memory.regions.MetaRegion;
import com.universe.touchpoint.memory.regions.TransportRegion;

import java.util.HashMap;
import java.util.Map;

public class TouchPointMemory {

    private static final Map<Region, TouchPointRegion> regions = new HashMap<>();
    static {
        regions.put(Region.TRANSPORT, TouchPointRegion.getInstance(TransportRegion.class));
        regions.put(Region.META, TouchPointRegion.getInstance(MetaRegion.class));
    }

    public static <R extends TouchPointRegion> R getRegion(Region region) {
        return (R) regions.get(region);
    }

    public static void clear() {
        regions.clear();
    }

}
