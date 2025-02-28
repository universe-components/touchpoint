package com.universe.touchpoint.transport.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.context.TaskContext;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ActionExecutionSelector;
import com.universe.touchpoint.plan.ActionExecutor;
import com.universe.touchpoint.plan.ResultExchanger;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointBroadcastReceiver<T extends TouchPoint> extends BroadcastReceiver {

    private final Class<T> tpClass;

    public TouchPointBroadcastReceiver(Class<?> tpClass) {
        this.tpClass = (Class<T>) tpClass;
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

        ((ActionExecutor<T>) ActionExecutionSelector.getExecutor(touchPoint)).execute(touchPoint, context);
        ResultExchanger.exchange(touchPoint, taskContext.getGoal(), taskName, context, Transport.BROADCAST);
    }

}
