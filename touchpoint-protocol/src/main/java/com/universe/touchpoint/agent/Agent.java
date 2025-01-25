package com.universe.touchpoint.agent;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.qihoo360.i.Factory;
import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePluginEnv;
import com.qihoo360.replugin.RePluginHost;
import com.qihoo360.replugin.model.PluginInfo;
import com.universe.touchpoint.annotations.TouchPointAgent;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.utils.AnnotationUtils;

import java.util.Objects;

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

    public static String getName() {
        return (String) getProperty("name");
    }

    public static Model getModel() {
        return (Model) getProperty("model");
    }

    public static Object getProperty(String propertyName) {
        Context context = RePluginEnv.getPluginContext();
        if (context == null) {
            context = AppVar.sAppContext;
        }

        Application application = (Application) context;
        Class<?> applicationClass = application.getClass();
        if (applicationClass.isAnnotationPresent(TouchPointAgent.class)) {
            // 获取注解实例
            TouchPointAgent annotation = applicationClass.getAnnotation(TouchPointAgent.class);
            // 返回注解的属性值
            assert annotation != null;
            return Objects.requireNonNull(
                    AnnotationUtils.getAnnotationValue(annotation, TouchPointAgent.class, propertyName));
        }

        return null;
    }

    public static Context fetchContext(String name) {
        return Factory.queryPluginContext(name);
    }

    public static PackageInfo fetchPackageInfo(String name) {
        return Factory.queryPluginPackageInfo(name);
    }

}
