package com.universe.touchpoint.channel.rpc;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.channel.TouchPointChannel;

public class TouchPointDubboChannel implements TouchPointChannel {

    @Override
    public <T extends TouchPoint> boolean send(T touchpoint) {
        // Implement RPC call to send touchpoint

        return false;
    }

}
