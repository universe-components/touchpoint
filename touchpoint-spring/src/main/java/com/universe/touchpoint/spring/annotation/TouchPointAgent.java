package com.universe.touchpoint.spring.annotation;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import com.universe.touchpoint.spring.AgentRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AgentRegistrar.class)
public @interface TouchPointAgent {

    String name();

    String desc() default "";

    String iconName() default "";

    SocketProtocol socketBindProtocol() default SocketProtocol.MQTT5;

}
