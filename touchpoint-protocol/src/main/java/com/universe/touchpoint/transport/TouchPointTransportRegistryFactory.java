package com.universe.touchpoint.transport;

import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.transport.broadcast.TouchPointBroadcastReceiverRegistry;
import com.universe.touchpoint.transport.rpc.TouchPointDubboRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouchPointTransportRegistryFactory {

    private static final Map<Transport, Class<? extends TouchPointTransportRegistry>> registryMap = new HashMap<>();
    static {
        registryMap.put(Transport.DUBBO, TouchPointDubboRegistry.class);
    }

    public static TouchPointTransportRegistry createRegistry(Transport transport) {
        try {
            if (transport == null) {
                return new TouchPointBroadcastReceiverRegistry();
            }
            return Objects.requireNonNull(registryMap.get(transport)).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
