package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.transport.Dubbo;
import com.universe.touchpoint.annotations.transport.MQTT;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.config.transport.MQTTConfig;
import com.universe.touchpoint.config.transport.rpc.DubboConfig;

import org.apache.dubbo.config.annotation.DubboService;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TransportConfigMapping {

    public static final Map<Transport, Class<?>> transport2Config = new HashMap<>();
    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    public static final Map<Class<? extends Annotation>, Transport> annotation2Type = new HashMap<>();
    static {
        transport2Config.put(Transport.DUBBO, DubboConfig.class);
        annotation2Config.put(DubboService.class, DubboConfig.class);
        annotation2Config.put(Dubbo.class, DubboConfig.class);
        annotation2Type.put(DubboService.class, Transport.DUBBO);
        annotation2Config.put(MQTT.class, MQTTConfig.class);
        annotation2Type.put(MQTT.class, Transport.MQTT);
        transport2Config.put(Transport.MQTT, MQTTConfig.class);
    }

    public static Set<Class<? extends Annotation>> getAnnotationClasses() {
        return annotation2Config.keySet();
    }

}
