package com.universe.touchpoint.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.qihoo360.i.Factory;
import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePluginEnv;
import com.qihoo360.replugin.RePluginHost;
import com.qihoo360.replugin.model.PluginInfo;
import com.universe.touchpoint.annotations.task.TouchPointAgent;
import com.universe.touchpoint.utils.AnnotationUtils;
import java.lang.annotation.Annotation;

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

    public static Context getContext() {
        Context context = RePluginEnv.getPluginContext();
        if (context == null) {
            context = AppVar.sAppContext;
        }
        return context;
    }

    public static String getName() {
        return (String) getProperty("name", TouchPointAgent.class);
    }

    public static String[] getTasks() {
        return (String[]) getProperty("tasks", TouchPointAgent.class);
    }

    public static Object getProperty(String propertyName, Class<? extends Annotation> annotationClass) {
        Context context = Agent.getContext();

        Application application = (Application) context;
        Class<?> applicationClass = application.getClass();

        // 检查传入的注解类是否存在于 applicationClass 中
        if (applicationClass.isAnnotationPresent(annotationClass)) {
            // 获取注解实例
            Annotation annotation = applicationClass.getAnnotation(annotationClass);
            // 返回注解的属性值
            return AnnotationUtils.getAnnotationValue(annotation, annotationClass, propertyName);
        }

        return null;
    }

    public static boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        Context context = Agent.getContext();

        Application application = (Application) context;
        Class<?> applicationClass = application.getClass();

        // 检查传入的注解类是否存在于 applicationClass 中
        return applicationClass.isAnnotationPresent(annotationClass);
    }

    public static Class<?> getApplicationClass() {
        Context context = Agent.getContext();

        Application application = (Application) context;
        return application.getClass();
    }

    public static Context fetchContext(String name) {
        return Factory.queryPluginContext(name);
    }

    public static PackageInfo fetchPackageInfo(String name) {
        return Factory.queryPluginPackageInfo(name);
    }

}
