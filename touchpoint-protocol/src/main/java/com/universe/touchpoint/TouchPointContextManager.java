package com.universe.touchpoint;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.annotations.TouchPointListener;
import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.channel.TouchPointChannelManager;
import com.universe.touchpoint.channel.TouchPointReceiverManager;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;
import com.universe.touchpoint.provider.TouchPointProvider;
import com.universe.touchpoint.router.AgentRouterManager;
import com.universe.touchpoint.utils.ApkUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TouchPointContextManager {

    private static final HashMap<String, TouchPointContext> contexts = new HashMap<>();
    private static final Object lock = new Object();

    public static void initContext() {
        synchronized(lock) {
            String name = Agent.getName();
            String ctxName = TouchPointHelper.touchPointPluginName(name);
            contexts.put(ctxName, new TouchPointContext());
        }
    }

    public static TouchPointContext getContext(String name) {
        return contexts.get(name);
    }

    public static <T extends TouchPoint> T generateTouchPoint(Class<T> tpClass, String name) {
        Context ctx = TouchPointContext.getAgentContext();
        return generateTouchPoint(tpClass, name, ctx);
    }

    public static <A extends TouchPoint> A generateTouchPoint(
            A action, String name, String content) {
        Context ctx = TouchPointContext.getAgentContext();
        TouchPointChannel channel = TouchPointChannelManager.defaultChannel(ctx);

        action.setHeader(
                new TouchPoint.Header(Agent.getName(), name, channel));
        action.setGoal(content);

        return action;
    }

    public static <T> T generateTouchPoint(Class<T> tpClass, String name, Context context) {
        try {
            TouchPointChannel channel = TouchPointChannelManager.defaultChannel(context);
            T selfTouchPoint = tpClass.getConstructor().newInstance();
            Class<?> touchPointClass = tpClass.getSuperclass();

            assert touchPointClass != null;
            Method setNameMethod = touchPointClass.getDeclaredMethod("setToAgent", String.class);
            setNameMethod.invoke(selfTouchPoint, name);

            Method setChannelMethod = touchPointClass.getDeclaredMethod("setChannel", TouchPointChannel.class);
            setChannelMethod.invoke(selfTouchPoint, channel);

            return selfTouchPoint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public static void registerTouchPointReceivers(Context appContext, boolean isPlugin, ConfigType configType) {
        try {
//            Bundle metaData = null;
            String name;
            /* if (configType == ConfigType.XML) {
                if (isPlugin) {
                    metaData = appContext.getApplicationInfo().metaData;
                    name = RePlugin.getPluginName();
                } else {
                    ApplicationInfo appInfo = appContext.getPackageManager().getApplicationInfo(
                            appContext.getPackageName(), PackageManager.GET_META_DATA);
                    metaData = appInfo.metaData;
                    name = RePlugin.getHostName(appContext);
                }
            } else {*/
                name = Agent.getName();
//            }

            List<String> receiverClassList;
            List<Object> receiverFilterList;
            Model actionModel;
            /* if (configType == ConfigType.XML) {
                // 获取存储的字符串列表
                String receiverClasses = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVERS);
                String receiverFilters = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVER_FILTERS);
                assert receiverClasses != null;
                receiverClassList = Arrays.asList(receiverClasses.replace(" ", "").split(","));
                assert receiverFilters != null;
                receiverFilterList = Arrays.asList(receiverFilters.replace(" ", "").split(","));
            } else {*/
                List<Pair<String, List<Object>>> receiverFilterPair = ApkUtils.getClassNames(appContext, TouchPointListener.class, Arrays.asList("fromAgent", "model"), !isPlugin);
                receiverClassList = receiverFilterPair.stream()
                        .map(pair -> pair.first)
                        .toList();
                receiverFilterList = receiverFilterPair.stream()
                        .map(pair -> pair.second.get(0))
                        .toList();
                actionModel = (Model) receiverFilterPair.stream()
                        .map(pair -> pair.second.get(1))
                        .toList().get(0);
//            }

            if (receiverClassList.size() == receiverFilterList.size()) {
                for (int i = 0; i < receiverClassList.size(); i++) {
                    // 动态注册接收器，并传递相应的过滤器
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        TouchPointReceiverManager.getInstance().registerTouchPointReceiver(
                                appContext,
                                name,
                                (String[]) receiverFilterList.get(i),
                                AgentActionManager.getInstance().extractAndRegisterAction(
                                        receiverClassList.get(i), (String[]) receiverFilterList.get(i), actionModel, name)
                        );
                    }
                    AgentRouterManager.registerRouteEntry((String[]) receiverFilterList.get(i), appContext);
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

    public static <T extends TouchPoint> T fetchTouchPoint(String filter, Class<T> clazz) {
        String contentProviderUri = TouchPointHelper.touchPointContentProviderUri(
                TouchPointConstants.CONTENT_PROVIDER_PREFIX,
                filter);
        TouchPointContent touchPointContent = TouchPointContentFactory.createContent(
                Uri.parse(contentProviderUri),
                TouchPointContext.getAgentContext());

        return touchPointContent.query(clazz);
    }

}
