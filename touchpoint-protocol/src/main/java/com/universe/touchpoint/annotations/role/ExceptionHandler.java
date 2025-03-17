package com.universe.touchpoint.annotations.role;

import com.universe.touchpoint.exception.TouchPointException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {

  String task();

  int errorCode();

  Class<?> exceptionClass() default TouchPointException.class;

  String scopeAction();
}
