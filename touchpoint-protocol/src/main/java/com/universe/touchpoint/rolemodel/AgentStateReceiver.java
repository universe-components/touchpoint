package com.universe.touchpoint.rolemodel;

import com.universe.touchpoint.negotiation.AgentContext;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.negotiation.AgentSocketStateRouter;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class AgentStateReceiver implements AgentReceiver {

    @Override
    public <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic) {
        new AgentSocketStateRouter<>().route(context, message.getPayload(), topic);
    }

}
