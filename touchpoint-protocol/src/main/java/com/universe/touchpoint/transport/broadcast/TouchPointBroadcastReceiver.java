package com.universe.touchpoint.transport.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointExecutor;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultExchanger;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.router.RouteTable;
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

        String filter = TouchPointHelper.touchPointFilterName(touchPoint.getHeader().getFromAction().getActionName());

        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        TouchPointExecutor<T, ?> tpReceiver = (TouchPointExecutor<T, ?>) transportRegion.getTouchPointReceiver(filter);

        if (touchPoint instanceof AgentAction) {
            ((AgentAction<I, O>) touchPoint).setOutput((O) tpReceiver.run(
                    (T) ((AgentAction<I, O>) touchPoint).getInput(), context));
        } else if(touchPoint instanceof AgentFinish) {
            List<AgentActionMetaInfo> predecessors = RouteTable.getInstance().getPredecessors(touchPoint.getHeader().getFromAction().getActionName());
            if (predecessors == null) {
                tpReceiver.run(touchPoint, context);
            }
        } else {
            tpReceiver.run(touchPoint, context);
        }
        ResultExchanger.exchange(
                touchPoint, touchPoint.goal, null, mContext, Transport.BROADCAST);
    }

}
