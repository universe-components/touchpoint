package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.config.transport.MQTTConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;

import java.util.HashMap;
import java.util.Map;

public class TouchPointMQTT5Registry implements TouchPointTransportRegistry<MQTTConfig> {

    private MqttClient client;
    private final Map<String, TouchPointMQTT5Subscriber<?, ?, ?>> messageSubscribers = new HashMap<>();

    @Override
    public void init(Context context, MQTTConfig transportConfig) {
        try {
            client = new MqttClient(transportConfig.brokerUri, "touchpoint_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(Context context, AgentActionMetaInfo agentActionMetaInfo, String previousAction, String task) {
        try {
            client.subscribe(TouchPointHelper.touchPointFilterName(previousAction), 1, (topic, message) -> {
                // 接收到消息时的回调
                TouchPointMQTT5Subscriber<?, ?, ?> subscriber = messageSubscribers.get(topic);
                assert subscriber != null;
                subscriber.handleMessage(message, context);
            });
            messageSubscribers.put(
                    TouchPointHelper.touchPointFilterName(previousAction),
                    new TouchPointMQTT5Subscriber<>(AgentAction.class));
            TouchPointChannelManager.registerContextReceiver(agentActionMetaInfo.getActionName(), agentActionMetaInfo.getClassName(), task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MqttClient getClient() {
        return client;
    }

}
