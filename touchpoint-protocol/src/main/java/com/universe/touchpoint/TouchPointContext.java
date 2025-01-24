package com.universe.touchpoint;

import android.content.ContentProvider;
import android.content.Context;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;

import java.util.HashMap;

public class TouchPointContext {

    private final HashMap<String, TouchPointListener<?, ?>> touchPointReceivers = new HashMap<>();
    private final HashMap<String, ContentProvider> touchPointProviders = new HashMap<>();
    private final HashMap<String, Action> touchPointActions = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointListener<?, ?> receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointListener<?, ?> getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

    public HashMap<String, TouchPointListener<?, ?>> getTouchPointReceivers() {
        return touchPointReceivers;
    }

    public void putTouchPointProvider(String uri, ContentProvider provider) {
        touchPointProviders.put(uri, provider);
    }

    public ContentProvider getTouchPointProvider(String uri) {
        return touchPointProviders.get(uri);
    }

    public void putTouchPointAction(String name, Action action) {
        touchPointActions.put(name, action);
    }

    public Action getTouchPointAction(String name) {
        return touchPointActions.get(name);
    }

    public static Context getAgentContext() {
        if (AppVar.sAppContext != null) {
            return AppVar.sAppContext;
        }
        return RePlugin.getPluginContext();
    }

}
