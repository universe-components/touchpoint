package com.universe.touchpoint.config;

import com.universe.touchpoint.annotations.TouchPointAction;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class ActionConfigMeta {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(TouchPointAction.class, ActionConfig.class);
    }

}
