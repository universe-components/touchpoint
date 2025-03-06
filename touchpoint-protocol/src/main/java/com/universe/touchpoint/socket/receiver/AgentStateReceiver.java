package com.universe.touchpoint.socket.receiver;

import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketReceiver;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class AgentStateReceiver implements AgentSocketReceiver {

    @Override
    public <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic) {
        new AgentSocketStateRouter<>().route(context, message.getPayload(), topic);
    }

}
