package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.sync.AgentReceiver;
import com.universe.touchpoint.utils.SerializeUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class AgentContextReceiver implements AgentReceiver {

    @Override
    public <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic) {
        TouchPointContext ctx = SerializeUtils.deserializeFromByteArray(message.getPayload(), TouchPointContext.class);
        CoordinatorFactory.getCoordinator(context.getBelongTask()).execute(ctx);
    }

}
