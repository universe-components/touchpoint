package com.universe.touchpoint.annotations.task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPointAction {

  String name();

  String desc();

  int errorCode() default 0;

  OperateType operateType() default OperateType.EXECUTE_ACTION;

  String[] toAgents() default {};

  String[] toActions() default {};
}
