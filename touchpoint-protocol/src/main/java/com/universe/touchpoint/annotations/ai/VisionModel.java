package com.universe.touchpoint.annotations.ai;

import com.universe.touchpoint.config.ai.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VisionModel {

    Model name() default Model.NONE;

    float temperature() default 0.0f;

}
