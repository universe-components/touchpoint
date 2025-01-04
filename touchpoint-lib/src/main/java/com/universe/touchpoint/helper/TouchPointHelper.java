package com.universe.touchpoint.helper;

import com.universe.touchpoint.TouchPointConstants;

public class TouchPointHelper {
    public static String touchPointPluginName(String name) {
        return TouchPointConstants.PLUGIN_NAME_PREFIX + "." + name;
    }

    public static String touchPointReceiverClassName(String type) {
        return String.format(TouchPointConstants.TOUCH_POINT_PKG + ".%sBroadcastReceiver", type);
    }

    public static String touchPointSubscriberClassName(String type) {
        return String.format(TouchPointConstants.TOUCH_POINT_PKG + ".%sSubscriber", type);
    }

}
