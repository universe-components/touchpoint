package com.universe.touchpoint.channel.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.Agent;
import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.TouchPointListener;
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
        String filter = TouchPointHelper.touchPointFilterName(touchPoint.filter);
        TouchPointListener<T> tpReceiver = (TouchPointListener<T>) TouchPointContextManager.getContext(ctxName).getTouchPointReceiver(filter);
        tpReceiver.onReceive(touchPoint, mContext);
    }

}
