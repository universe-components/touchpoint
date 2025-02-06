package com.universe.touchpoint.transport.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessorAdapter;
import com.universe.touchpoint.memory.Region;
import com.universe.touchpoint.memory.TouchPointMemory;
import com.universe.touchpoint.memory.regions.TransportRegion;
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

        String filter = TouchPointHelper.touchPointFilterName(touchPoint.getHeader().getFromAction());

        TransportRegion transportRegion = TouchPointMemory.getRegion(Region.TRANSPORT);
        TouchPointListener<T, ?> tpReceiver = (TouchPointListener<T, ?>) transportRegion.getTouchPointReceiver(filter);

        ResultProcessorAdapter.getProcessor(
                touchPoint, touchPoint.goal, null, tpReceiver, mContext, Transport.BROADCAST).process();
    }

}
