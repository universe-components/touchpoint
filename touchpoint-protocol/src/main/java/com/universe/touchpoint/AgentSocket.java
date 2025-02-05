package com.universe.touchpoint;

import com.qihoo360.replugin.RePluginHost;

public class AgentSocket {

//    public static AgentSocket getInstance() {
//        synchronized (mLock) {
//            if (mInstance == null) {
//                mInstance = new AgentSocket();
//            }
//            return mInstance;
//        }
//    }

    public static void bind(String apkName) {
        RePluginHost.install(apkName);
    }

    public static void unbind(String apkName) {
        RePluginHost.uninstall(apkName);
//        AgentReporter.getInstance("clean").report(AgentCleaner.AgentClean.ALL, context);
    }

}
