package com.universe.touchpoint;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;
import com.universe.touchpoint.helper.TouchPointHelper;

import java.util.HashMap;

public class TouchPointContext {

    private final HashMap<String, TouchPointListener<?>> touchPointReceivers = new HashMap<>();
    private final HashMap<String, ContentProvider> touchPointProviders = new HashMap<>();

    public void putTouchPointReceiver(String filter, TouchPointListener<?> receiver) {
        touchPointReceivers.put(filter, receiver);
    }

    public TouchPointListener<?> getTouchPointReceiver(String filter) {
        return touchPointReceivers.get(filter);
    }

    public HashMap<String, TouchPointListener<?>> getTouchPointReceivers() {
        return touchPointReceivers;
    }

    public void putTouchPointProvider(String uri, ContentProvider provider) {
        touchPointProviders.put(uri, provider);
    }

    public ContentProvider getTouchPointProvider(String uri) {
        return touchPointProviders.get(uri);
    }

    public static Context getAgentContext() {
        if (AppVar.sAppContext != null) {
            return AppVar.sAppContext;
        }
        return RePlugin.getPluginContext();
    }

    public static String getAgentName() {
        String name = RePlugin.getPluginName();
        if (name == null) {
            name = RePlugin.getHostName(AppVar.sAppContext);
        }
        return name;
    }

}
