package com.universe.touchpoint.arp;

import com.universe.touchpoint.Agent;
import com.universe.touchpoint.AgentEntity;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.ai.AIModelResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AgentRouter {

    private static final Object lock = new Object();
    protected final static Map<String, List<AgentRouteEntry>> routeTable = new HashMap<>();

    public static AgentRouteEntry routeTo(AIModelResponse.AgentAction action) {
        List<AgentRouteEntry> agentRouteEntries = routeTable.get(Agent.getProperty("name"));
        if (agentRouteEntries == null || agentRouteEntries.isEmpty()) {
            return null;
        }

        // 解析 choice，获取 choice中需要的数据
        for (AgentRouteEntry routeItem : agentRouteEntries) {
            if (action.getAction().contains(routeItem.getToAgent().getName())) {
                return routeItem;
            }
        }

        return null;
    }

    public static <T extends TouchPoint> void addRoute(String fromAgent, AgentEntity toAgent, Class<T> sharedClass) {
        synchronized (lock) {
            AgentRouteEntry routeItem = new AgentRouteEntry();
            routeItem.setFromAgent(fromAgent);
            routeItem.setToAgent(toAgent);
            routeItem.setSharedClass(sharedClass);

            if (!routeTable.containsKey(fromAgent)) {
                routeTable.put(fromAgent, new ArrayList<>());
            }

            Objects.requireNonNull(routeTable.get(fromAgent)).add(routeItem);
        }
    }

    public static String buildChunk(String fromAgent, String toAgent) {
        return fromAgent + "->" + toAgent;
    }

    public static String buildChunk(AgentRouteEntry routeItem) {
        return routeItem.getFromAgent() + "->" +routeItem.getToAgent().getName();
    }

    public static String[] splitChunk(String chunk) {
        return chunk.split("->");
    }

    public static List<AgentRouteEntry> routeItems(String fromAgent) {
        return routeTable.getOrDefault(fromAgent, new ArrayList<>());
    }

    public static boolean hasFromAgent(String toAgent) {
        for (List<AgentRouteEntry> routeItems : routeTable.values()) {
            for (AgentRouteEntry routeItem : routeItems) {
                if (routeItem.getToAgent().getName().equals(toAgent)) {
                    return true;
                }
            }
        }
        return false;
    }

}
