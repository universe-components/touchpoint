package com.universe.touchpoint.channel;

import com.universe.touchpoint.TouchPoint;

public interface TouchPointChannel {

    <T extends TouchPoint> boolean send(T touchpoint);

}
