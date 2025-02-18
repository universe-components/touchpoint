package com.universe.touchpoint.monitor.listener;

import android.content.Context;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.metric.MetricSocketConfig;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.monitor.MetricListener;
import com.universe.touchpoint.monitor.TaskMetricManager;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.Map;

public class MQTT5MetricListener implements MetricListener {

    private MqttClient client;

    @Override
    public void initialize(MetricSocketConfig socketConfig) {
        try {
            client = new MqttClient(socketConfig.getBrokerUri(), "metric_socket_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(Map<String, ?> metrics, Context context, String task) {
        String topic = TouchPointHelper.touchPointFilterName(TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_TASK_METRIC_FILTER,
                task
        ));
        try {
            MqttMessage message = new MqttMessage(SerializeUtils.serializeToByteArray(metrics));
            client.publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerReceiver(Context appContext, String task) {
        try {
            client.subscribe(TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_TASK_METRIC_FILTER,
                    task), 1, (topic, message) -> {
                if (message == null) {
                    return;
                }
                Map<String, Integer> metrics = SerializeUtils.deserializeFromByteArray(message.getPayload(), Map.class);
                for (Map.Entry<String, Integer> entry : metrics.entrySet()) {
                    if (entry.getKey().contains("||")) {
                        String[] taskActionPair = entry.getKey().split("\\|\\|");
                        TaskMetricManager.getActionMetric(taskActionPair[0], taskActionPair[1]).setPredictionCount(entry.getValue());
                    } else {
                        TaskMetricManager.getTaskMetric(entry.getKey()).setRetryActionCount(entry.getValue());
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
