package com.universe.touchpoint.transport.mqtt;

import android.content.Context;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultExchanger;
import com.universe.touchpoint.rolemodel.TaskExecutorFactory;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
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
        TaskContext task = touchPoint.getContext().getTask();

        if (touchPoint instanceof AgentAction) {
            int stateCode = ((AgentAction<I, O>) touchPoint).getInput().getState().getCode();
            if (stateCode >= 300 && stateCode < 400) {
                CoordinatorFactory.getCoordinator(task.getTaskName()).execute((AgentAction<I, O>) touchPoint, task.getTaskName(), context);
            } else if (stateCode >= 400) {
                SupervisorFactory.getSupervisor(task.getTaskName()).execute((AgentAction<I, O>) touchPoint, task.getTaskName(), context);
            }

            RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskExecutorFactory.getInstance(task.getTaskName()).getExecutor(((AgentAction<?, ?>) touchPoint).getActionName());
            O runResult = tpReceiver.run(((AgentAction<I, O>) touchPoint).getInput(), context);

            // If redirecting, rebuild the ActionGraph.
            if (runResult.getState().getRedirectToAction() != null
                    && !RouteTable.getInstance().containsItem(((AgentAction<?, ?>) touchPoint).getActionName(), runResult.getState().getRedirectToAction())) {
                ((AgentAction<I, O>) touchPoint).getMeta().getToActions().addToAction(task.getTaskName(), touchPoint.getState().getRedirectToAction());
                AgentSocketStateMachine.getInstance(task.getTaskName()).send(
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.PARTICIPANT_READY, ((AgentAction<?, ?>) touchPoint).getMeta()),
                        context,
                        task.getTaskName()
                );
            }
            ((AgentAction<I, O>) touchPoint).setOutput(runResult);
        } else if(touchPoint instanceof AgentFinish) {
            List<AgentActionMetaInfo> predecessors = RouteTable.getInstance().getPredecessors(touchPoint.getHeader().getFromAction().getActionName());
            if (predecessors == null) {
                RoleExecutor<T, O> tpReceiver = (RoleExecutor<T, O>) TaskExecutorFactory.getInstance(task.getTaskName()).getExecutor(topic);
                tpReceiver.run(touchPoint, context);
            }
        } else {
            RoleExecutor<T, O> tpReceiver = (RoleExecutor<T, O>) TaskExecutorFactory.getInstance(task.getTaskName()).getExecutor(topic);
            tpReceiver.run(touchPoint, context);
        }
        ResultExchanger.exchange(touchPoint, task.getGoal(), null, null, Transport.BROADCAST);
    }

}
