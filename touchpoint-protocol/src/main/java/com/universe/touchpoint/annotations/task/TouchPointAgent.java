package com.universe.touchpoint.annotations.task;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TouchPointAgent {

    String name();

    String desc() default "";

    String iconName() default "";

    SocketProtocol socketBindProtocol() default SocketProtocol.MQTT5;

}
