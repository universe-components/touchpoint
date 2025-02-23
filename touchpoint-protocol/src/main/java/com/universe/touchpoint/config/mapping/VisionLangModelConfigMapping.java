package com.universe.touchpoint.config.mapping;

import com.universe.touchpoint.annotations.ai.VisionLangModel;
import com.universe.touchpoint.config.ai.VisionLangModelConfig;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class VisionLangModelConfigMapping {

    public static final Map<Class<? extends Annotation>, Class<?>> annotation2Config = new HashMap<>();
    static {
        annotation2Config.put(VisionLangModel.class, VisionLangModelConfig.class);
    }

}
