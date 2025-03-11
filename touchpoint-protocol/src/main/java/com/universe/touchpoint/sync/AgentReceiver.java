package com.universe.touchpoint.sync;

import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.utils.SerializeUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public abstract class AgentReceiver<M> {

    public <C extends AgentContext> void handleMessage(C context, M message, String topic, Class<M> messageType) {
        M actualMessage = message;
        if (message instanceof MqttMessage) {
            actualMessage = SerializeUtils.deserializeFromByteArray(((MqttMessage) message).getPayload(), messageType);
        }
        handleMessage(context, actualMessage, topic);
    }

    public abstract <C extends AgentContext> void handleMessage(C context, M message, String topic);

}
