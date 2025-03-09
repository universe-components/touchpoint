package com.universe.touchpoint.sync;

import com.universe.touchpoint.negotiation.AgentContext;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public interface AgentReceiver {

    <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic);

}
