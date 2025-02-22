package com.universe.touchpoint.binder.mode;

import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.binder.Binder;

public class AndroidBinder implements Binder {

    @Override
    public void bind(String apkPath) {
        RePluginHost.install(apkPath);
        RePluginHost.preload(apkPath);
    }

    @Override
    public void unbind(String apkPath) {
        RePluginHost.uninstall(apkPath);
    }

}
