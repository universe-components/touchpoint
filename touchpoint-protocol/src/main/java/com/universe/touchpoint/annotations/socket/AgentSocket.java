package com.universe.touchpoint.annotations.socket;

import com.universe.touchpoint.annotations.transport.SocketProtocol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AgentSocket {

    SocketProtocol bindProtocol() default SocketProtocol.ANDROID_BROADCAST;
    String brokerUri() default "";

}
