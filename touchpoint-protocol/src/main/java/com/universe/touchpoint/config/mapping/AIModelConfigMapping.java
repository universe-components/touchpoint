package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.ai.AIModel;
import com.universe.touchpoint.config.ai.AIModelConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class AIModelConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(AIModel.class, AIModelConfig.class);
    }

}
