package com.universe.touchpoint.annotations.task;

import com.universe.touchpoint.annotations.role.ActionRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPointAction {

    String name();

    String desc();

    ActionRole role() default ActionRole.EXECUTOR;

    String[] toAgents() default {};

    String[] toActions() default {};

}
