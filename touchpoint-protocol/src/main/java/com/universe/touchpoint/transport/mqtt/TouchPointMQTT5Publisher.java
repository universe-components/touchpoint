package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointTransportRegistryFactory;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TouchPointMQTT5Publisher extends TouchPointChannel<Boolean> {

    public TouchPointMQTT5Publisher(Context context) {
        super(context);
    }

    @Override
    public Boolean send(TouchPoint touchpoint) {
        TouchPointMQTT5Registry mqtt5Registry = (TouchPointMQTT5Registry) TouchPointTransportRegistryFactory.getRegistry(Transport.MQTT);
        MqttMessage message = new MqttMessage(SerializeUtils.serializeToByteArray(touchpoint));
        message.setQos(1);  // 设置 QoS 为1

        String topic = TouchPointHelper.touchPointFilterName(touchpoint.getHeader().getFromAction().actionName());
        try {
            mqtt5Registry.getClient().publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

}
