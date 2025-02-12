package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TouchPointMQTT5Subscriber<T extends TouchPoint> {

    private final Class<T> tpClass;

    public TouchPointMQTT5Subscriber(Class<?> tpClass) {
        this.tpClass = (Class<T>) tpClass;
    }


    public void handleMessage(String topic, MqttMessage message, Context context) {
        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) transportRegion.getTouchPointReceiver(topic);
        tpReceiver.onReceive(SerializeUtils.deserializeFromByteArray(message.getPayload(), tpClass), context);
    }

}
