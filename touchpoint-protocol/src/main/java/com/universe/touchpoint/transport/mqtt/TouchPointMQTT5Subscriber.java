package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultExchanger;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.List;

public class TouchPointMQTT5Subscriber<T extends TouchPoint, I extends TouchPoint, O extends TouchPoint> {

    private final Class<T> tpClass;

    public TouchPointMQTT5Subscriber(Class<?> tpClass) {
        this.tpClass = (Class<T>) tpClass;
    }


    public void handleMessage(String topic, MqttMessage message, Context context) {
        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) transportRegion.getTouchPointReceiver(topic);
        T touchPoint = SerializeUtils.deserializeFromByteArray(message.getPayload(), tpClass);

        if (touchPoint instanceof AgentAction) {
            ((AgentAction<I, O>) touchPoint).setOutput((O) tpReceiver.onReceive(
                    (T) ((AgentAction<I, O>) touchPoint).getActionInput(), context));
        } else if(touchPoint instanceof AgentFinish) {
            List<AgentActionMetaInfo> predecessors = RouteTable.getInstance().getPredecessors(touchPoint.getHeader().getFromAction().getActionName());
            if (predecessors == null) {
                tpReceiver.onReceive(touchPoint, context);
            }
        } else {
            tpReceiver.onReceive(touchPoint, context);
        }
        ResultExchanger.exchange(
                touchPoint, touchPoint.goal, null, null, Transport.BROADCAST);
    }

}
