package com.universe.touchpoint;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.qihoo360.mobilesafe.api.AppVar;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.annotations.TouchPointListener;
import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.ApkUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TouchPointContextManager {

    private static TouchPointContext context;
    private static final Object lock = new Object();

    public static TouchPointContext getContext() {
        synchronized(lock) {
            if (context == null) {
                context = new TouchPointContext();
            }

            return context;
        }
    }

    public static <T extends TouchPoint> T generateTouchPoint(Class<T> tpClass) {
        String name = RePlugin.getPluginName();
        Context ctx = RePlugin.getPluginContext();
        if (name == null) {
            name = RePlugin.getHostName(AppVar.sAppContext);
            ctx = AppVar.sAppContext;
        }
        return generateTouchPoint(tpClass, name, ctx);
    }

    public static <T> T generateTouchPoint(Class<T> tpClass, Context context) {
        String name = RePlugin.getPluginName();
        if (name == null) {
            name = RePlugin.getHostName(AppVar.sAppContext);
        }
        return generateTouchPoint(tpClass, name, context);
    }

    public static <T> T generateTouchPoint(Class<T> tpClass, String name, Context context) {
        try {
            TouchPointChannel channel = TouchPointChannelManager.defaultChannel(context);
            T selfTouchPoint = tpClass.getConstructor().newInstance();
            Class<?> touchPointClass = tpClass.getSuperclass();

            assert touchPointClass != null;
            Method setNameMethod = touchPointClass.getDeclaredMethod("setName", String.class);
            setNameMethod.invoke(selfTouchPoint, name);

            Method setChannelMethod = touchPointClass.getDeclaredMethod("setChannel", TouchPointChannel.class);
            setChannelMethod.invoke(selfTouchPoint, channel);

            return selfTouchPoint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static void registerTouchPointReceivers(Context appContext, boolean isPlugin, ConfigType configType) {
        try {
            Bundle metaData = null;
            if (isPlugin) {
                metaData = appContext.getApplicationInfo().metaData;
            } else {
                ApplicationInfo appInfo = appContext.getPackageManager().getApplicationInfo(
                        appContext.getPackageName(), PackageManager.GET_META_DATA);
                metaData = appInfo.metaData;
            }

            List<String> receiverClassList = null;
            List<String> receiverFilterList = null;
            if (configType == ConfigType.XML) {
                // 获取存储的字符串列表
                String receiverClasses = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVERS);
                String receiverFilters = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVER_FILTERS);
                assert receiverClasses != null;
                receiverClassList = Arrays.asList(receiverClasses.replace(" ", "").split(","));
                assert receiverFilters != null;
                receiverFilterList = Arrays.asList(receiverFilters.replace(" ", "").split(","));
            } else {
                List<Pair<String, String>> receiverFilterPair = ApkUtils.getClassNames(appContext, TouchPointListener.class, "from");
                receiverClassList = receiverFilterPair.stream()
                        .map(pair -> pair.first) // 提取每个 Pair 的第一个值
                        .collect(Collectors.toList());
                receiverFilterList = receiverFilterPair.stream()
                        .map(pair -> pair.second) // 提取每个 Pair 的第二个值
                        .collect(Collectors.toList());
            }

            if (receiverClassList.size() == receiverFilterList.size()) {
                for (int i = 0; i < receiverClassList.size(); i++) {
                    String name = TouchPointHelper.touchPointPluginName(receiverFilterList.get(i));

                    // 动态注册接收器，并传递相应的过滤器
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        TouchPointReceiverManager.getInstance().registerTouchPointReceiver(
                                appContext,
                                name,
                                receiverClassList.get(i)
                        );
                    }
                }
            } else {
                // 处理不匹配的情况，例如打印日志或抛出异常
                Log.e("ReceiverRegistration", "Receiver classes and filters list sizes do not match.");
            }
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

}
