package com.universe.touchpoint.annotations;

import com.universe.touchpoint.config.Model;

public @interface AIModel {

    Model value() default Model.NONE;

    float temperature() default 0.0f;

}
