package com.universe.touchpoint;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.qihoo360.i.Factory;
import com.qihoo360.replugin.RePluginHost;
import com.qihoo360.replugin.model.PluginInfo;

public class Agent {

    public static Drawable getIcon(String name) {
        try {
            // 获取插件信息
            PluginInfo pluginInfo = RePluginHost.getPluginInfo(name);
            if (pluginInfo == null) {
                return null;
            }

            // 获取插件资源路径
            String pluginResourcePackage = pluginInfo.getPackageName();
            Context pluginContext = RePluginHost.fetchContext(name);

            // 使用 RePlugin 获取插件资源中的图标
            int iconResId = pluginContext.getResources().getIdentifier(
                    name + "_icon", "drawable", pluginResourcePackage
            );

            return pluginContext.getResources().getDrawable(iconResId); // 返回插件图标资源 ID
        } catch (Exception e) {
            return null;
        }
    }

    public static Context fetchContext(String name) {
        return Factory.queryPluginContext(name);
    }

    public static PackageInfo fetchPackageInfo(String name) {
        return Factory.queryPluginPackageInfo(name);
    }

    public static boolean connect(String filePath) {
        PluginInfo pluginInfo = RePluginHost.install(filePath);
        return pluginInfo != null;
    }

}
