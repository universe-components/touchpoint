package com.universe.touchpoint.negotiation;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TouchPointContext;
import com.universe.touchpoint.plan.ActionGraph;
import com.universe.touchpoint.plan.ActionGraphBuilder;
import com.universe.touchpoint.security.processor.JWTProcessor;
import com.universe.touchpoint.sync.AgentReceiver;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class AgentStateReceiver implements AgentReceiver<MqttMessage> {

    @Override
    public <C extends AgentContext> void handleMessage(C context, MqttMessage message, String topic) {
        boolean rs = new AgentSocketStateRouter<>().route(context, message.getPayload(), topic);
        if (rs) {
            TouchPoint touchPoint = new TouchPoint();
            ActionGraph actionGraph = ActionGraphBuilder.getTaskGraph(context.getBelongTask());
            TouchPointContext tpCtx = new TouchPointContext(context.getBelongTask());
            String token = new JWTProcessor<>().generateToken(actionGraph);
            tpCtx.setToken(token);
            touchPoint.setContext(tpCtx);
            AgentSocketStateMachine.getInstance(context.getBelongTask()).getCallbackListener().onResponse(touchPoint);
        }
    }

}
