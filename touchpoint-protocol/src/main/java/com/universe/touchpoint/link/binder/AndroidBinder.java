package com.universe.touchpoint.link.binder;

import com.qihoo360.replugin.RePluginHost;
import com.universe.touchpoint.link.Binder;

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
