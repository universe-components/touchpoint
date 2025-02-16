package com.universe.touchpoint.socket.protocol;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.config.socket.AgentSocketConfig;
import com.universe.touchpoint.context.AgentContext;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.socket.AgentSocketProtocol;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class MQTT5 implements AgentSocketProtocol {

    private MqttClient client;

    @Override
    public void initialize(@NonNull AgentSocketConfig socketConfig) {
        try {
            client = new MqttClient(socketConfig.getBrokerUri(), "agent_socket_mqtt_broker");
            MqttConnectionOptions connectOptions = new MqttConnectionOptions();
            connectOptions.setCleanStart(true);
            client.connect(connectOptions);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(AgentSocketStateMachine.AgentSocketStateContext<?> stateContext, Context context, String filterSuffix) {
        String topic = TouchPointHelper.touchPointFilterName(TouchPointHelper.touchPointFilterName(
                TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                filterSuffix
        ));
        try {
            MqttMessage message = new MqttMessage(SerializeUtils.serializeToByteArray(stateContext));
            client.publish(topic, message);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <C extends AgentContext> void registerReceiver(Context appContext, @Nullable C context) {
        try {
            assert context != null;
            client.subscribe(TouchPointHelper.touchPointFilterName(
                    TouchPointConstants.TOUCH_POINT_TASK_STATE_FILTER,
                    context.getBelongTask()), 1, (topic, message) -> {
                if (message == null) {
                    return;
                }
                new AgentSocketStateRouter<>().route(context, appContext, message.getPayload(), context.getBelongTask());
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
