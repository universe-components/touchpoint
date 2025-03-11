package com.universe.touchpoint.transport.mqtt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.config.transport.MQTTConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.transport.TouchPointTransportRegistry;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;

public class TouchPointMQTT5Registry implements TouchPointTransportRegistry<MQTTConfig> {

  private MqttClient client;

  @Override
  public void init(MQTTConfig transportConfig) {
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
  public void register(
      AgentActionMeta agentActionMeta, String previousAction, String task, boolean isRequested) {
    try {
      String filter =
          String.join(
              "/",
              "$share",
              agentActionMeta.getName(),
              TouchPointHelper.touchPointFilterName(previousAction));
      client.subscribe(
          filter,
          1,
          (topic, message) -> {
            // 接收到消息时的回调
            new TouchPointMQTT5Subscriber<>(isRequested ? AgentAction.class : AgentFinish.class)
                .handleMessage(message);
          });
      TouchPointChannelManager.registerContextReceiver(
          agentActionMeta.getName(), agentActionMeta.getClassName(), task);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public MqttClient getClient() {
    return client;
  }
}
