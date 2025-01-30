package com.universe.touchpoint;

import android.content.Context;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;

public class TouchPointContext {

    public static Context getAgentContext() {
        if (AppVar.sAppContext != null) {
            return AppVar.sAppContext;
        }
        return RePlugin.getPluginContext();
    }

}
