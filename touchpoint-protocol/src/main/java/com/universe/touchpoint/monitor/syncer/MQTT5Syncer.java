package com.universe.touchpoint.monitor.syncer;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.monitor.MetricSyncer;
import com.universe.touchpoint.monitor.metric.ActionMetric;
import com.universe.touchpoint.monitor.metric.TaskMetric;
import com.universe.touchpoint.utils.SerializeUtils;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import io.moquette.broker.Server;
import io.moquette.broker.config.MemoryConfig;

public class MQTT5Syncer implements MetricSyncer {

    private MqttClient client;

    @Override
    public void initialize(MetricSocketConfig config) {
        try {
            if (config.getBrokerUri().contains("localhost")) {
                Server mqttBroker = new Server();
                Properties configProps = new Properties();
                configProps.setProperty("port", "1883");
                mqttBroker.startServer(new MemoryConfig(configProps));
            }
            client = new MqttClient(config.getBrokerUri(), "agent_socket_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerListener(String task) {
        try {
            client.subscribe(TouchPointHelper.touchPointFilterName(
                    task,
                    TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER),
                    1, (topic, message) -> {
                if (message == null) {
                    return;
                }
                Pair<TaskMetric, Map<String, ActionMetric>> metricPair = SerializeUtils.deserializeFromByteArray(message.getPayload(), Pair.class);
                TouchPointContextManager.getTouchPointContext(task).getTaskContext().setMetric(metricPair.getLeft());
                TouchPointContextManager.getTouchPointContext(task).getActionContext().setActionMetrics(metricPair.getRight());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMetrics(Pair<TaskMetric, Map<String, ActionMetric>> metricPair, String task) {
        String topic = TouchPointHelper.touchPointFilterName(TouchPointHelper.touchPointFilterName(
                task,
                TouchPointConstants.METRIC_FILTER
        ));
        try {
            MqttMessage message = new MqttMessage(SerializeUtils.serializeToByteArray(metricPair));
            client.publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

}
