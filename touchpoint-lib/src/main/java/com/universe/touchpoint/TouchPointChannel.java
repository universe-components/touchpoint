package com.universe.touchpoint;

public interface TouchPointChannel {

    <T extends TouchPoint> boolean send(T touchpoint);

}
