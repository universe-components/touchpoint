package com.universe.touchpoint.socket.protocol;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.selector.AgentSocketReceiverSelector;
import com.universe.touchpoint.utils.SerializeUtils;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;

public class MQTT5Protocol implements AgentSocketProtocol {

    private MqttClient client;

    @Override
    public void initialize(@Nonnull AgentSocketConfig socketConfig) {
        try {
            if (socketConfig.getBrokerUri().contains("localhost")) {
                Server mqttBroker = new Server();
                Properties configProps = new Properties();
                configProps.setProperty("port", "1883");
                mqttBroker.startServer(new MemoryConfig(configProps));
            }
            client = new MqttClient(socketConfig.getBrokerUri(), "agent_socket_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <M> void send(M stateContext, String filter) {
        try {
            MqttMessage message = new MqttMessage(SerializeUtils.serializeToByteArray(stateContext));
            client.publish(filter, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <C extends AgentContext> void registerReceiver(@Nullable C context, String filter, RoleType role) {
        try {
            assert context != null;
            String socketFilter = TouchPointHelper.touchPointFilterName(filter, context.getBelongTask(), role.name());
            client.subscribe(socketFilter, 1, (topic, message) -> {
                if (message == null) {
                    return;
                }
                Objects.requireNonNull(AgentSocketReceiverSelector.selectReceiver(filter)).handleMessage(context, message, topic);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
