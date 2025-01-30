package com.universe.touchpoint.provider;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;

import com.universe.touchpoint.TouchPointConstants;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TouchPointContentFactory {

    @SuppressLint("StaticFieldLeak")
    private static TouchPointContent content;
    private static final Object lock = new Object();

    public static TouchPointContent createContent(Uri uri, Context context) {
        synchronized(lock) {
            if (content == null) {
                content = new TouchPointContent(uri, context);
            }
        }
        return content;
    }

    public static void registerContentProvider(Context context) {
        try {
            // 获取 ActivityThread 类
            @SuppressLint("PrivateApi") Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            // 获取当前的 ActivityThread 实例
            @SuppressLint("DiscouragedPrivateApi") Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取 installProvider 方法
            @SuppressLint("DiscouragedPrivateApi") Method installProviderMethod = activityThreadClass.getDeclaredMethod(
                    "installContentProviders",
                    Context.class,
                    List.class
            );
            installProviderMethod.setAccessible(true);

            // 创建 ContentProvider 实例
            ContentProvider provider = new TouchPointProvider();

            // 设置 ProviderInfo
            ProviderInfo providerInfo = new ProviderInfo();
            List<ProviderInfo> providerInfos = new ArrayList<>();
            providerInfo.authority = TouchPointConstants.CONTENT_PROVIDER_PREFIX;
            providerInfo.name = provider.getClass().getName();
            providerInfo.applicationInfo = context.getApplicationInfo();
            providerInfo.packageName = context.getPackageName();
            providerInfos.add(providerInfo);

            // 调用 installProvider 方法
            installProviderMethod.invoke(
                    currentActivityThread,
                    context,
                    providerInfos
            );

            // 调用 ContentProvider 的 onCreate 方法进行初始化
            provider.attachInfo(context, providerInfo);
            provider.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
