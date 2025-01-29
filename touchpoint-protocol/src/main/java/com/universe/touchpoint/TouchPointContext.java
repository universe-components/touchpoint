package com.universe.touchpoint;

import android.content.ContentProvider;
import android.content.Context;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionMeta;
import com.universe.touchpoint.config.ActionConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TouchPointContext {

    private final HashMap<String, TouchPointAction> touchPointReceivers = new HashMap<>();
    private final HashMap<String, ContentProvider> touchPointProviders = new HashMap<>();
    private final HashMap<String, AgentActionMeta> touchPointActions = new HashMap<>();
    private final HashMap<String, List<ActionConfig>> touchPointTasks = new HashMap<>();

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

    public void putTouchPointAction(String name, AgentActionMeta agentActionMeta) {
        touchPointActions.put(name, agentActionMeta);
    }

    public AgentActionMeta getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public void addTouchPointTaskAction(ActionConfig action) {
        touchPointTasks.computeIfAbsent(Agent.getName(), k -> new ArrayList<>()).add(action);
    }

    public List<ActionConfig> getTouchPointTaskActions(String agentName) {
        return touchPointTasks.get(agentName);
    }

    public static Context getAgentContext() {
        if (AppVar.sAppContext != null) {
            return AppVar.sAppContext;
        }
        return RePlugin.getPluginContext();
    }

}
