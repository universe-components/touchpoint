package com.universe.touchpoint.sync.protocol;

import com.universe.touchpoint.annotations.role.RoleType;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.sync.AgentReceiverSelector;
import com.universe.touchpoint.sync.AgentSyncProtocol;
import com.universe.touchpoint.utils.SerializeUtils;
import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

public class MQTT5Protocol implements AgentSyncProtocol<MqttMessage> {

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
  public void send(MqttMessage message, String filter) {
    try {
      MqttMessage mqttMessage = new MqttMessage(SerializeUtils.serializeToByteArray(message));
      client.publish(filter, mqttMessage);
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <C extends AgentContext> void registerReceiver(
      @Nullable C context, String filter, RoleType role, Class<MqttMessage> messageType) {
    try {
      assert context != null;
      String socketFilterAll =
          TouchPointHelper.touchPointFilterName(
              String.join(".", filter, "all"), context.getBelongTask(), role.name());
      String socketFilter =
          TouchPointHelper.touchPointFilterName(filter, context.getBelongTask(), role.name());
      String[] socketTopics = {socketFilter, socketFilterAll};
      client.setCallback(
          new MqttCallback() {
            @Override
            public void disconnected(MqttDisconnectResponse disconnectResponse) {}

            @Override
            public void mqttErrorOccurred(MqttException exception) {}

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
              if (message == null) {
                return;
              }
              ((AgentReceiver<MqttMessage>)
                      Objects.requireNonNull(AgentReceiverSelector.selectReceiver(filter)))
                  .handleMessage(context, message, topic, messageType);
            }

            @Override
            public void deliveryComplete(IMqttToken token) {}

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {}

            @Override
            public void authPacketArrived(int reasonCode, MqttProperties properties) {}
          });
      client.subscribe(socketTopics, new int[] {1, 1});
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
