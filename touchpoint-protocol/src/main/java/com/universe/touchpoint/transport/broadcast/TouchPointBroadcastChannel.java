package com.universe.touchpoint.transport.broadcast;

import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointBroadcastChannel extends TouchPointChannel<Boolean> {

    public TouchPointBroadcastChannel(Context context) {
        super(context);
    }

    @Override
    public <T extends TouchPoint, O extends TouchPoint> Boolean send(T touchpoint) {
        String filter = TouchPointHelper.touchPointFilterName(touchpoint.getHeader().getFromAction().getActionName());
        Intent intent = new Intent(filter);
        intent.putExtra(TouchPointConstants.TOUCH_POINT_EVENT_NAME, SerializeUtils.serializeToByteArray(touchpoint));
        context.sendBroadcast(intent);
        return true;
    }

}
