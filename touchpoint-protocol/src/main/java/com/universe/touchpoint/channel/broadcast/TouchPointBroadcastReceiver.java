package com.universe.touchpoint.channel.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.Agent;
import com.universe.touchpoint.Dispatcher;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.TouchPointListener;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.arp.AgentRouter;
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

        String name = Agent.getProperty("name");
        String ctxName = TouchPointHelper.touchPointPluginName(name);
        String filter = TouchPointHelper.touchPointFilterName(touchPoint.getHeader().getToAgent());
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) TouchPointContextManager.getContext(ctxName).getTouchPointReceiver(filter);

        if (!(touchPoint instanceof AIModelResponse.AgentAction)
                && !(touchPoint instanceof AIModelResponse.AgentFinish)) {
            tpReceiver.onReceive(touchPoint, mContext);
        }

        if (touchPoint instanceof AIModelResponse.AgentAction) {
            String actionResult = tpReceiver.onReceive(touchPoint, mContext).toString();
            ((AIModelResponse.AgentAction) touchPoint).setObservation(actionResult);
            Dispatcher.loopCall((AIModelResponse.AgentAction) touchPoint, touchPoint.content, intent.getAction());
        }
        if (touchPoint instanceof AIModelResponse.AgentFinish) {
            if (!AgentRouter.hasFromAgent(touchPoint.getHeader().getFromAgent())) {
                tpReceiver.onReceive(touchPoint, mContext);
                return;
            }
            TouchPointContextManager.generateTouchPoint(
                    AIModelResponse.AgentFinish.class,
                    AgentRouter.buildChunk(
                            touchPoint.getHeader().getFromAgent(),
                            touchPoint.getHeader().getToAgent())
            ).finish();
        }
    }

}
