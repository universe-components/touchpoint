package com.universe.touchpoint.channel.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.TouchPointReceiver;
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
        byte[] touchPointBytes = intent.getByteArrayExtra("touch_point");

        T touchPoint = SerializeUtils.deserializeFromByteArray(touchPointBytes, tpClass);

        String name = TouchPointHelper.touchPointPluginName(touchPoint.name);
        TouchPointReceiver<T> tpReceiver = (TouchPointReceiver<T>) TouchPointContextManager.getContext().getTouchPointReceiver(name);
        tpReceiver.onReceive(touchPoint, mContext);
    }

}
