package com.universe.touchpoint.socket.receiver;

import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.context.TouchPointContextManager;
import com.universe.touchpoint.socket.AgentContext;
import com.universe.touchpoint.socket.AgentSocketReceiver;
import com.universe.touchpoint.utils.SerializeUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class AgentContextReceiver implements AgentSocketReceiver {

    @Override
    public <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic) {
        TouchPointContext ctx = SerializeUtils.deserializeFromByteArray(message.getPayload(), TouchPointContext.class);
        TouchPointContextManager.putTouchPointContext(context.getBelongTask(), ctx);
    }

}
