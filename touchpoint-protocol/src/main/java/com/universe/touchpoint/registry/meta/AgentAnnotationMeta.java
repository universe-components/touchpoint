package com.universe.touchpoint.registry.meta;

import com.universe.touchpoint.registry.BaseAnnotationMeta;
import java.util.Map;

public class AgentAnnotationMeta extends BaseAnnotationMeta {

    public AgentAnnotationMeta(Class<?> importingClass, Map<String, Object> attributes) {
        super(importingClass, attributes);
    }

}
