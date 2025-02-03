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
import com.universe.touchpoint.utils.AnnotationUtils;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TouchPointTransportConfigBroadcaster<C> extends AgentBroadcaster<TransportConfig<C>> {

    public static <T> Map<Transport, T> agentConfig() {
        try {
            Map<Transport, T> transportConfigMap = (Map<Transport, T>) AnnotationUtils.annotation2Config(
                    Agent.getApplicationClass(),
                    TransportConfigMapping.annotation2Config,
                    TransportConfigMapping.annotation2Type);

            if (!transportConfigMap.isEmpty()) {
                return transportConfigMap;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (AgentBuilder.getBuilder() == null) {
            return null;
        }

        return (Map<Transport, T>) Collections.singletonMap(
                AgentBuilder.getBuilder().getConfig().getTransportConfig().transportType(),
                AgentBuilder.getBuilder().getConfig().getTransportConfig().config());
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
