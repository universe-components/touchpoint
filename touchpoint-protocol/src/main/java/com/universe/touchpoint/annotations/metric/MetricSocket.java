package com.universe.touchpoint.annotations.metric;

import com.universe.touchpoint.annotations.socket.SocketProtocol;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MetricSocket {

    SocketProtocol bindProtocol() default SocketProtocol.MQTT5;
    String brokerUri() default "";

}
