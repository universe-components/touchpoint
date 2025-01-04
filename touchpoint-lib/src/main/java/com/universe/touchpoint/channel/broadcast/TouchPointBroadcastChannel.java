package com.universe.touchpoint.channel.broadcast;

import android.content.Context;
import android.content.Intent;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.utils.SerializeUtils;

public class TouchPointBroadcastChannel implements TouchPointChannel {

    private final Context context;

    public TouchPointBroadcastChannel(Context context) {
        this.context = context;
    }

    @Override
    public <T extends TouchPoint> boolean send(T touchpoint) {
        String name = TouchPointHelper.touchPointPluginName(touchpoint.name);
        Intent intent = new Intent(name);
        intent.putExtra("touch_point", SerializeUtils.serializeToByteArray(touchpoint));
        context.sendBroadcast(intent);
        return true;
    }

}
