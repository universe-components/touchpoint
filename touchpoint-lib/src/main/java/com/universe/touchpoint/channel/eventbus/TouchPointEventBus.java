package com.universe.touchpoint.channel.eventbus;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointChannel;

import org.greenrobot.eventbus.EventBus;

public class TouchPointEventBus implements TouchPointChannel {

    private final EventBus eventBus;

    public TouchPointEventBus() {
        this.eventBus = new EventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Override
     public <T extends TouchPoint> boolean send(T touchpoint) {
        eventBus.post(touchpoint);
        return true;
    }

}
