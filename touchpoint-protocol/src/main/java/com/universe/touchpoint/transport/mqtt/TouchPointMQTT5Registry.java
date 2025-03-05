package com.universe.touchpoint.transport.mqtt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.meta.AgentActionMeta;
import com.universe.touchpoint.config.transport.MQTTConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;

public class TouchPointMQTT5Registry implements TouchPointTransportRegistry<MQTTConfig> {

    private MqttClient client;
    private final Map<String, TouchPointMQTT5Subscriber<?>> messageSubscribers = new ConcurrentHashMap<>();

    @Override
    public void init(MQTTConfig transportConfig) {
        try {
            if (transportConfig.brokerUri.contains("localhost")) {
                Server mqttBroker = new Server();
                Properties configProps = new Properties();
                configProps.setProperty("port", "1883");
                mqttBroker.startServer(new MemoryConfig(configProps));
            }
            client = new MqttClient(transportConfig.brokerUri, "touchpoint_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(AgentActionMeta agentActionMeta, String previousAction, String task, boolean isRequested) {
        try {
            client.subscribe(TouchPointHelper.touchPointFilterName(previousAction), 1, (topic, message) -> {
                // 接收到消息时的回调
                TouchPointMQTT5Subscriber<?> subscriber = messageSubscribers.get(topic);
                assert subscriber != null;
                subscriber.handleMessage(message);
            });
            messageSubscribers.put(
                    TouchPointHelper.touchPointFilterName(previousAction),
                    new TouchPointMQTT5Subscriber<>(isRequested ? AgentAction.class : AgentFinish.class));
            TouchPointChannelManager.registerContextReceiver(agentActionMeta.getName(), agentActionMeta.getClassName(), task);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MqttClient getClient() {
        return client;
    }

}
