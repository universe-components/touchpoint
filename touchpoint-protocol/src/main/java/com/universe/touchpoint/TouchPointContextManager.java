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
import com.universe.touchpoint.annotations.AIModel;
import com.universe.touchpoint.annotations.TouchPointAction;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.ActionConfigMeta;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.TransportConfigMeta;
import com.universe.touchpoint.task.TaskManager;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;
import com.universe.touchpoint.provider.TouchPointProvider;
import com.universe.touchpoint.router.AgentRouterManager;
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.ApkUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        TouchPointChannel channel = TouchPointChannelManager.selectChannel(action, ctx);

        action.setHeader(new TouchPoint.Header(Agent.getName(), name, channel));
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
    public static void register(Context appContext, boolean isPlugin, ConfigType configType) {
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

            /* if (configType == ConfigType.XML) {
                // 获取存储的字符串列表
                String receiverClasses = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVERS);
                String receiverFilters = metaData.getString(TouchPointConstants.TOUCH_POINT_RECEIVER_FILTERS);
                assert receiverClasses != null;
                receiverClassList = Arrays.asList(receiverClasses.replace(" ", "").split(","));
                assert receiverFilters != null;
                receiverFilterList = Arrays.asList(receiverFilters.replace(" ", "").split(","));
            } else {*/
            List<Pair<String, List<Object>>> receiverFilterPair = ApkUtils.getClassNames(appContext,
                        TouchPointAction.class, Arrays.asList("name", "fromAgent", "fromAction"), !isPlugin);

            for (Pair<String, List<Object>> pair : receiverFilterPair) {
                String clazz = pair.first;  // 获取 String
                List<Object> properties = pair.second;  // 获取 List<Object>

                Map<Transport, Object> transportConfigMap = AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        TransportConfigMeta.annotation2Config,
                        TransportConfigMeta.annotation2Type
                );
                Transport transportType = transportConfigMap.keySet().iterator().next();
                Object transportConfig = transportConfigMap.get(transportType);
                // 动态注册接收器，并传递相应的过滤器
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Map<String, Object> annotationProperties = AnnotationUtils.getAnnotationValue(Class.forName(clazz), AIModel.class);
                    TouchPointRegistry.getInstance().register(
                            appContext,
                            name,
                            (String[]) properties.get(1),
                            (String[]) properties.get(2),
                            AgentActionManager.getInstance().extractAndRegisterAction(
                                    clazz,
                                    new AIModelConfig(
                                            (Model) Objects.requireNonNull(annotationProperties.get("name")),
                                            (float) annotationProperties.get("temperature")),
                                    new TransportConfig<>(
                                            transportType,
                                            transportConfig),
                                    (String) properties.get(0),
                                    name)
                    );
                }
                AgentRouterManager.registerRouteEntry((String[]) properties.get(1), appContext);
                ActionConfig actionConfig = (ActionConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        ActionConfigMeta.annotation2Config
                );
                assert actionConfig != null;
                TaskManager.registerTaskAction(actionConfig, appContext);
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
