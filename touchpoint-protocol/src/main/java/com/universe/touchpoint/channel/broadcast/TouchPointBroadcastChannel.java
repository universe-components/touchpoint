package com.universe.touchpoint.channel.broadcast;

import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointBroadcastChannel implements TouchPointChannel {

    private final Context context;

    public TouchPointBroadcastChannel(Context context) {
        this.context = context;
    }

    @Override
    public <T extends TouchPoint> boolean send(T touchpoint) {
        String filter = TouchPointHelper.touchPointFilterName(touchpoint.filter);
        Intent intent = new Intent(filter);
        intent.putExtra(TouchPointConstants.TOUCH_POINT_EVENT_NAME, SerializeUtils.serializeToByteArray(touchpoint));
        context.sendBroadcast(intent);
        return true;
    }

}
