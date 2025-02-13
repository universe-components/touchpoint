package com.universe.touchpoint.annotations;

public @interface AgentSocket {

    SocketProtocol bindProtocol() default SocketProtocol.ANDROID_BROADCAST;

}
