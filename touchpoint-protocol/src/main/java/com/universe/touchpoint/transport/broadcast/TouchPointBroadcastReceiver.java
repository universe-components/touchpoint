package com.universe.touchpoint.transport.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.api.RoleExecutor;
import com.universe.touchpoint.api.executor.AgentFinishExecutor;
import com.universe.touchpoint.api.executor.TouchPointExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.context.TouchPointState;
import com.universe.touchpoint.driver.ResultExchanger;
import com.universe.touchpoint.rolemodel.TaskRoleExecutor;
import com.universe.touchpoint.rolemodel.coordinator.CoordinatorFactory;
import com.universe.touchpoint.rolemodel.supervisor.SupervisorFactory;
import com.universe.touchpoint.router.RouteTable;
import com.universe.touchpoint.socket.AgentSocketState;
import com.universe.touchpoint.socket.AgentSocketStateMachine;
import com.universe.touchpoint.socket.AgentSocketStateRouter;
import com.universe.touchpoint.utils.SerializeUtils;

import java.util.List;

public class TouchPointBroadcastReceiver<T extends TouchPoint, I extends TouchPoint, O extends TouchPoint> extends BroadcastReceiver {

    private final Class<T> tpClass;
    private final Context mContext;

    public TouchPointBroadcastReceiver(Class<?> tpClass, Context context) {
        this.tpClass = (Class<T>) tpClass;
        this.mContext = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Context context, Intent intent) {
        byte[] touchPointBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_EVENT_NAME);
        T touchPoint = SerializeUtils.deserializeFromByteArray(touchPointBytes, tpClass);
        String taskName = touchPoint.getContext().getTask();
        TaskContext taskContext = touchPoint.getContext().getTaskContext();

        if (touchPoint.getState().getCode() < 200) {
            Log.i("CoordinateRelationEstablish", "Coordinate Relation Establishing...");
            return;
        }

        if (touchPoint instanceof AgentAction) {
            int stateCode = ((AgentAction<I, O>) touchPoint).getInput().getState().getCode();
            if (stateCode >= 300 && stateCode < 400) {
                CoordinatorFactory.getCoordinator(taskName).execute((AgentAction<I, O>) touchPoint, taskName, context);
                return;
            }
            if (stateCode >= 400) {
                SupervisorFactory.getSupervisor(taskName).execute((AgentAction<I, O>) touchPoint, taskName, context);
                // If redirecting, rebuild the ActionGraph.
                new AgentSocketStateRouter<>().route(
                        null,
                        context,
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, touchPoint),
                        taskName);
            } else {
                RoleExecutor<I, O> tpReceiver = (RoleExecutor<I, O>) TaskRoleExecutor.getInstance(taskName).getExecutor(((AgentAction<?, ?>) touchPoint).getActionName());
                O runResult = tpReceiver.run(((AgentAction<I, O>) touchPoint).getInput(), context);
                // If redirecting, rebuild the ActionGraph.
                new AgentSocketStateRouter<>().route(
                        null,
                        context,
                        new AgentSocketStateMachine.AgentSocketStateContext<>(AgentSocketState.REDIRECT_ACTION_READY, touchPoint),
                        taskName);
                touchPoint.setState(new TouchPointState(AgentSocketState.REDIRECT_ACTION_READY.getCode()));
                ((AgentAction<I, O>) touchPoint).setOutput(runResult);
            }
        } else if (touchPoint instanceof AgentFinish) {
            List<AgentActionMetaInfo> predecessors = RouteTable.getInstance().getPredecessors(touchPoint.getHeader().getFromAction().getActionName());
            if (predecessors == null) {
                AgentFinishExecutor finishExecutor = (AgentFinishExecutor) TaskRoleExecutor.getInstance(taskName)
                        .getExecutor(touchPoint.getHeader().getFromAction().getActionName());
                finishExecutor.run(((AgentFinish) touchPoint), context);
            }
        } else {
            TouchPointExecutor<T, O> touchPointExecutor = (TouchPointExecutor<T, O>) TaskRoleExecutor.getInstance(taskName)
                    .getExecutor(touchPoint.getHeader().getFromAction().getActionName());
            touchPointExecutor.run(touchPoint, context);
        }
        ResultExchanger.exchange(touchPoint, taskContext.getGoal(), taskName, mContext, Transport.BROADCAST);
    }

}
