package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.ai.VisionModel;
import com.universe.touchpoint.config.ai.VisionModelConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class VisionModelConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(VisionModel.class, VisionModelConfig.class);
    }

}
