package com.universe.touchpoint.transport.mqtt;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ActionExecutionSelector;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.plan.ResultExchanger;
import com.universe.touchpoint.utils.SerializeUtils;
import org.eclipse.paho.mqttv5.common.MqttMessage;

public class TouchPointMQTT5Subscriber<T extends TouchPoint> {

    private final Class<T> tpClass;

    public TouchPointMQTT5Subscriber(Class<?> tpClass) {
        this.tpClass = (Class<T>) tpClass;
    }


    public void handleMessage(MqttMessage message) {
        T touchPoint = SerializeUtils.deserializeFromByteArray(message.getPayload(), tpClass);
        String taskName = touchPoint.getContext().getTask();
        TaskContext taskContext = touchPoint.getContext().getTaskContext();

        touchPoint = ((ActionExecutor<T, ?>) ActionExecutionSelector.getExecutor(touchPoint)).execute(touchPoint);
        new ResultExchanger().exchange(touchPoint, taskContext.getGoal(), taskName, Transport.BROADCAST);
    }

}
