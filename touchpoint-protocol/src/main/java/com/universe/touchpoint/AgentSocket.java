package com.universe.touchpoint;

import com.qihoo360.replugin.RePluginHost;

public class AgentSocket {

    public static void bind(String apkName) {
        RePluginHost.install(apkName);
        RePluginHost.preload(apkName);
    }

    public static void unbind(String apkName) {
        RePluginHost.uninstall(apkName);
    }

}
