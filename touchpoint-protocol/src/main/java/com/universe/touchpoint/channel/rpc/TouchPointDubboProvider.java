package com.universe.touchpoint.channel.rpc;

public class TouchPointDubboProvider implements TouchPointDubboChannel.TouchPointService {

    @Override
    public <T> boolean invoke(T touchpoint) {
        return false;
    }

}
