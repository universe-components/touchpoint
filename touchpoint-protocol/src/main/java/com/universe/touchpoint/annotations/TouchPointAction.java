package com.universe.touchpoint.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPointAction {

    String name();

    String desc();

    String[] fromAgents() default {};

    String[] fromActions() default {};

    String[] taskProposers() default {};

}
