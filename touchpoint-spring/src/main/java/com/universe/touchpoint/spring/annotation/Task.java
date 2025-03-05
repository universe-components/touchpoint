package com.universe.touchpoint.spring.annotation;

import com.universe.touchpoint.spring.AgentTaskRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Import(AgentTaskRegistrar.class)
public @interface Task {

    String value();

}