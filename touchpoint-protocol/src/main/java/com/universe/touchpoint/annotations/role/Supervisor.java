package com.universe.touchpoint.annotations.role;

import com.universe.touchpoint.rolemodel.Scope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Supervisor {

    String task();

    Scope type();

}
