package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultExchanger;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import com.universe.touchpoint.utils.SerializeUtils;

import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.util.List;

public class TouchPointMQTT5Subscriber<T extends TouchPoint, I extends TouchPoint, O extends TouchPoint> {

    private final Class<T> tpClass;

    public TouchPointMQTT5Subscriber(Class<?> tpClass) {
        this.tpClass = (Class<T>) tpClass;
    }


    public void handleMessage(String topic, MqttMessage message, Context context) {
        T touchPoint = SerializeUtils.deserializeFromByteArray(message.getPayload(), tpClass);
        String task = touchPoint.getContext().getTask();
        TaskContext taskContext = touchPoint.getContext().getTaskContext();

        if (touchPoint instanceof AgentAction) {
            int stateCode = ((AgentAction<I, O>) touchPoint).getInput().getState().getCode();
            if (stateCode >= 300 && stateCode < 400) {
                CoordinatorFactory.getCoordinator(task).execute((AgentAction<I, O>) touchPoint, task, context);
            } else if (stateCode >= 400) {
                SupervisorFactory.getSupervisor(task).execute((AgentAction<I, O>) touchPoint, task, context);
                new AgentSocketStateRouter<>().route(
                        null,
                        context,
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, touchPoint),
                        task);
            } else {
                RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(task).getExecutor(((AgentAction<?, ?>) touchPoint).getActionName());
                O runResult = tpReceiver.run(((AgentAction<I, O>) touchPoint).getInput(), context);
                new AgentSocketStateRouter<>().route(
                        null,
                        context,
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, touchPoint),
                        task);
                ((AgentAction<I, O>) touchPoint).setOutput(runResult);
            }
        } else if(touchPoint instanceof AgentFinish) {
            List<AgentActionMetaInfo> predecessors = RouteTable.getInstance().getPredecessors(touchPoint.getHeader().getFromAction().getActionName());
            if (predecessors == null) {
                RoleExecutor<T, O> tpReceiver = (RoleExecutor<T, O>) TaskRoleExecutor.getInstance(task).getExecutor(topic);
                tpReceiver.run(touchPoint, context);
            }
        } else {
            RoleExecutor<T, O> tpReceiver = (RoleExecutor<T, O>) TaskRoleExecutor.getInstance(task).getExecutor(topic);
            tpReceiver.run(touchPoint, context);
        }
        ResultExchanger.exchange(touchPoint, taskContext.getGoal(), null, null, Transport.BROADCAST);
    }

}
