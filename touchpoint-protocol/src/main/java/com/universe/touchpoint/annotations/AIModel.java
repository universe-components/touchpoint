package com.universe.touchpoint.annotations;

import com.universe.touchpoint.config.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AIModel {

    Model name() default Model.NONE;

    float temperature() default 0.0f;

}
