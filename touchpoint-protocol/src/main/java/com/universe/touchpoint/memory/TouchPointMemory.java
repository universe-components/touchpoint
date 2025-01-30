package com.universe.touchpoint.memory;

import com.universe.touchpoint.memory.regions.RouteRegion;
import com.universe.touchpoint.memory.regions.DriverRegion;
import com.universe.touchpoint.memory.regions.TransportRegion;

import java.util.HashMap;
import java.util.Map;

public class TouchPointMemory {

    private static final Map<Region, TouchPointRegion> regions = new HashMap<>();
    private static final Object lock = new Object();

    public static void initialize() {
        synchronized(lock) {
            regions.put(Region.ROUTE, TouchPointRegion.getInstance(RouteRegion.class));
            regions.put(Region.TRANSPORT, TouchPointRegion.getInstance(TransportRegion.class));
            regions.put(Region.DRIVER, TouchPointRegion.getInstance(DriverRegion.class));
        }
    }

    public static <R extends TouchPointRegion> R getRegion(Region region) {
        return (R) regions.get(region);
    }

}
