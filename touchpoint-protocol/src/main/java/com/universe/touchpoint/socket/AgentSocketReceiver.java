package com.universe.touchpoint.socket;

import org.eclipse.paho.mqttv5.common.MqttMessage;

public interface AgentSocketReceiver {

    <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic);

}
