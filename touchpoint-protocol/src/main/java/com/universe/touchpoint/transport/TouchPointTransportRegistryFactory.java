package com.universe.touchpoint.transport;

import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.transport.broadcast.TouchPointBroadcastReceiverRegistry;
import com.universe.touchpoint.transport.mqtt.TouchPointMQTT5Registry;
import com.universe.touchpoint.transport.rpc.TouchPointDubboRegistry;

import java.util.HashMap;
import java.util.Map;

public class TouchPointTransportRegistryFactory {

    private static final Map<Transport, TouchPointTransportRegistry> registryMap = new HashMap<>();
    static {
        registryMap.put(Transport.DUBBO, new TouchPointDubboRegistry());
        registryMap.put(Transport.BROADCAST, new TouchPointBroadcastReceiverRegistry());
        registryMap.put(Transport.MQTT, new TouchPointMQTT5Registry());
    }

    public static TouchPointTransportRegistry getRegistry(Transport transport) {
        return registryMap.get(transport);
    }

}
