package com.universe.touchpoint.spring.annotation;

import com.universe.touchpoint.annotations.task.OperateType;
import com.universe.touchpoint.spring.AgentActionRegistrar;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AgentActionRegistrar.class)
public @interface TouchPointAction {

  String name();

  String desc();

  OperateType operateType() default OperateType.EXECUTE_ACTION;

  String[] toAgents() default {};

  String[] toActions() default {};
}
