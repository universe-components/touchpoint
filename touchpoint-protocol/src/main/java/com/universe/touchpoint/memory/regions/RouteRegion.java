package com.universe.touchpoint.memory.regions;

import com.universe.touchpoint.memory.TouchPointRegion;
import com.universe.touchpoint.router.AgentRouteEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RouteRegion extends TouchPointRegion {

    protected final static Map<String, List<AgentRouteEntry>> routeTable = new HashMap<>();

    public List<AgentRouteEntry> getRouteItems(String fromAgent) {
        return routeTable.get(fromAgent);
    }

    public void addRouteItem(String fromAgent, AgentRouteEntry routeItem) {
        if (!routeTable.containsKey(fromAgent)) {
            routeTable.put(fromAgent, new ArrayList<>());
        }
        Objects.requireNonNull(routeTable.get(fromAgent)).add(routeItem);
    }

    public Collection<List<AgentRouteEntry>> getAllRouteItems() {
        return routeTable.values();
    }

}
