package com.universe.touchpoint.transport;

import com.universe.touchpoint.TouchPoint;

public interface TouchPointChannel<R> {

    <I extends TouchPoint, O extends TouchPoint> R send(I touchpoint);

}
