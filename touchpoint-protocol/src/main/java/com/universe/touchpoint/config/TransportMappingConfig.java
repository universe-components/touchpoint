package com.universe.touchpoint.config;

import com.universe.touchpoint.config.transport.rpc.DubboConfig;

import org.apache.dubbo.config.annotation.DubboService;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class TransportMappingConfig {

    public static final Map<Transport, Class<?>> transport2Config = new HashMap<>();
    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    public static final Map<Class<? extends Annotation>, Transport> annotation2Type = new HashMap<>();
    static {
        transport2Config.put(Transport.DUBBO, DubboConfig.class);
        annotation2Config.put(DubboService.class, DubboConfig.class);
        annotation2Type.put(DubboService.class, Transport.DUBBO);
    }

}
