package com.universe.touchpoint;

import android.content.Context;
import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.qihoo360.replugin.helper.LogDebug;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.annotations.AIModel;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ActionConfig;
import com.universe.touchpoint.config.mapping.ActionConfigMapping;
import com.universe.touchpoint.config.Model;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.ApkUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class TouchPointRegistryCenter {

    private static final Object mLock = new Object();
    private static TouchPointRegistryCenter mInstance;

    public static TouchPointRegistryCenter getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new TouchPointRegistryCenter();
            }
            return mInstance;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    public void register(Context appContext, boolean isPlugin, ConfigType configType) {
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
                    com.universe.touchpoint.annotations.TouchPointAction.class, Arrays.asList("name", "fromAgents", "fromActions"), !isPlugin);

            for (Pair<String, List<Object>> pair : receiverFilterPair) {
                String clazz = pair.first;  // 获取 String
                List<Object> properties = pair.second;  // 获取 List<Object>

                Map<Transport, Object> transportConfigMap = AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        TransportConfigMapping.annotation2Config,
                        TransportConfigMapping.annotation2Type
                );
                Transport transportType = transportConfigMap.keySet().iterator().next();
                Object transportConfig = transportConfigMap.get(transportType);
                Map<String, Object> annotationProperties = AnnotationUtils.getAnnotationValue(Class.forName(clazz), AIModel.class);
                String[] filters = Stream.of((String[]) properties.get(1), (String[]) properties.get(2)).flatMap(Stream::of).toArray(String[]::new);

                AgentActionMetaInfo actionMetaInfo = AgentActionManager.getInstance().extractAndRegisterAction(
                        clazz,
                        new AIModelConfig(
                                (Model) Objects.requireNonNull(annotationProperties.get("name")),
                                (float) annotationProperties.get("temperature")),
                        new TransportConfig<>(
                                transportType,
                                transportConfig),
                        (String) properties.get(0),
                        name);

                AgentActionManager.getInstance().registerAgentFinishReceiver(
                        appContext,
                        filters,
                        actionMetaInfo.inputClass());

                TouchPointTransportRegistryFactory
                        .createRegistry(actionMetaInfo.transportConfig().transportType())
                        .register(
                            appContext,
                            actionMetaInfo,
                            filters
                        );

                TouchPointChannelManager.registerContextReceiver(filters, actionMetaInfo);

                ActionConfig actionConfig = (ActionConfig) AnnotationUtils.annotation2Config(
                        Class.forName(clazz),
                        ActionConfigMapping.annotation2Config
                );
                assert actionConfig != null;
                ActionReporter.getInstance("taskAction").report(actionConfig, appContext);
                ActionReporter.getInstance("router").report((String[]) properties.get(1), appContext);

                ActionReporter.getInstance("taskAction").registerReceiver(appContext);
                AgentBroadcaster.getInstance("aiModel").registerReceiver(appContext);
                AgentBroadcaster.getInstance("transportConfig").registerReceiver(appContext);
                ActionReporter.getInstance("router").registerReceiver(appContext);
            }
        } catch (Exception e) {
            if (LogDebug.LOG) {
                e.printStackTrace();
            }
        }
    }

}
