package com.universe.touchpoint.transport;

import com.universe.touchpoint.context.TouchPoint;

public interface TouchPointChannel<R> {

    <I extends TouchPoint, O extends TouchPoint> R send(I touchpoint);

}
