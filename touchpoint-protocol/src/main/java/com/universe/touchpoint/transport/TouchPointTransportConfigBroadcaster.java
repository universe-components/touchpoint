package com.universe.touchpoint.transport;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.AgentBroadcaster;
import com.universe.touchpoint.AgentSocket;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.TransportConfig;
import com.universe.touchpoint.config.mapping.TransportConfigMapping;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.RouteRegion;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.utils.SerializeUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class TouchPointTransportConfigBroadcaster<C> extends AgentBroadcaster<TransportConfig<C>> {

    public static <T> TransportConfig<T> agentConfig(Transport transport) {
        try {
            Class<?> configClass = TransportConfigMapping.transport2Config.get(transport);
            assert configClass != null;
            String annotationClassName = "com.universe.touchpoint.annotations." + configClass.getSimpleName().replace("Config", "");
            Class<?> annotationClass = Class.forName(annotationClassName);

            String applicationName = (String) Agent.getProperty("applicationName", (Class<Annotation>) annotationClass);
            if (applicationName != null) {
                String registryAddress = (String) Agent.getProperty("registryAddress", (Class<Annotation>) annotationClass);
                return (TransportConfig<T>) new TransportConfig<>(
                        transport,
                        configClass.getDeclaredConstructor(String.class, String.class).newInstance(applicationName, registryAddress));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (AgentBuilder.getBuilder() == null) {
            return null;
        }
        return (TransportConfig<T>) AgentBuilder.getBuilder().getConfig().getTransportConfig();
    }

    @Override
    public void send(TransportConfig<C> config, Context context) {
        RouteRegion routeRegion = TouchPointMemory.getRegion(Region.ROUTE);
        List<AgentRouteEntry> routeEntries = routeRegion.getRouteItems(Agent.getName());

        for (AgentRouteEntry entry : routeEntries) {
            String transportConfigAction = TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_FILTER_NAME,
                    entry.getToAgent().getName());

            Intent transportConfigIntent = new Intent(transportConfigAction);
            transportConfigIntent.putExtra(TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_EVENT_NAME,
                    SerializeUtils.serializeToByteArray(config));

            context.sendBroadcast(transportConfigIntent);
        }
    }

    @Override
    public void registerReceiver(Context context) {
        IntentFilter filter = new IntentFilter(
                TouchPointHelper.touchPointFilterName(
                        TouchPointConstants.TOUCH_POINT_TRANSPORT_CONFIG_FILTER_NAME,
                        Agent.getName()));
        context.registerReceiver(
                new TouchPointTransportConfigReceiver(AgentSocket.getInstance()),
                filter,
                Context.RECEIVER_EXPORTED);
    }

}
