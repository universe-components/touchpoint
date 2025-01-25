package com.universe.touchpoint.annotations;

import com.universe.touchpoint.config.Model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPointAgent {

    String name();

    String desc() default "";

    Model model() default Model.o1;

    String iconName() default "";

}
