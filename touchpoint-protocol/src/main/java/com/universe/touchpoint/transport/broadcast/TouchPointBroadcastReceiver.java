package com.universe.touchpoint.transport.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.Dispatcher;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
import com.universe.touchpoint.router.AgentRouter;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointBroadcastReceiver<T extends TouchPoint> extends BroadcastReceiver {

    private final Class<T> tpClass;
    private final Context mContext;

    public TouchPointBroadcastReceiver(Class<T> tpClass, Context context) {
        this.tpClass = tpClass;
        this.mContext = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onReceive(Context context, Intent intent) {
        byte[] touchPointBytes = intent.getByteArrayExtra(TouchPointConstants.TOUCH_POINT_EVENT_NAME);

        T touchPoint = SerializeUtils.deserializeFromByteArray(touchPointBytes, tpClass);

        String filter = TouchPointHelper.touchPointFilterName(touchPoint.getHeader().getToAgent());

        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) transportRegion.getTouchPointReceiver(filter);

        if (!(touchPoint instanceof AgentAction)
                && !(touchPoint instanceof AgentFinish)) {
            tpReceiver.onReceive(touchPoint, mContext);
        }

        if (touchPoint instanceof AgentAction) {
            String actionResult = tpReceiver.onReceive(
                    (T) ((AgentAction) touchPoint).getActionInput(), mContext).toString();
            ((AgentAction) touchPoint).setObservation(actionResult);
            Dispatcher.loopCall((AgentAction) touchPoint, touchPoint.goal, intent.getAction());
        }
        if (touchPoint instanceof AgentFinish) {
            if (!AgentRouter.hasFromAgent(touchPoint.getHeader().getFromAgent())) {
                tpReceiver.onReceive(touchPoint, mContext);
                return;
            }
            TouchPointContextManager.generateTouchPoint(
                    AgentFinish.class,
                    AgentRouter.buildChunk(
                            touchPoint.getHeader().getFromAgent(),
                            touchPoint.getHeader().getToAgent())
            ).finish();
        }
    }

}
