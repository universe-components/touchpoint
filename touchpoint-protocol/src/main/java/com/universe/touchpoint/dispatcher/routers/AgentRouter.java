package com.universe.touchpoint.dispatcher.routers;

import com.universe.touchpoint.Agent;
import com.universe.touchpoint.AgentEntity;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.dispatcher.AgentRouteItem;
import com.universe.touchpoint.dispatcher.Router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AgentRouter implements Router<AIModelResponse.AgentAction, AgentRouteItem> {

    private final static Map<String, List<AgentRouteItem>> routeTable = new HashMap<>();
    private static final Object lock = new Object();

    public AgentRouteItem routeTo(AIModelResponse.AgentAction action) {
        List<AgentRouteItem> agentRouteItems = routeTable.get(Agent.getProperty("name"));
        if (agentRouteItems == null || agentRouteItems.isEmpty()) {
            return null;
        }

        // 解析 choice，获取 choice中需要的数据
        for (AgentRouteItem routeItem : agentRouteItems) {
            if (action.getAction().contains(routeItem.getToAgent().getName())) {
                return routeItem;
            }
        }

        return null;
    }

    public static <T extends TouchPoint> void addRoute(String fromAgent, AgentEntity toAgent, Class<T> sharedClass) {
        synchronized (lock) {
            AgentRouteItem routeItem = new AgentRouteItem();
            routeItem.setFromAgent(fromAgent);
            routeItem.setToAgent(toAgent);
            routeItem.setSharedClass(sharedClass);

            if (!routeTable.containsKey(fromAgent)) {
                routeTable.put(fromAgent, new ArrayList<>());
            }

            Objects.requireNonNull(routeTable.get(fromAgent)).add(routeItem);
        }
    }

    public List<AgentRouteItem> agentRouteItems(String fromAgent) {
        return routeTable.getOrDefault(fromAgent, new ArrayList<>());
    }

}
