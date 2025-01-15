package com.universe.touchpoint.helper;

import com.universe.touchpoint.TouchPointConstants;

public class TouchPointHelper {
    public static String touchPointPluginName(String name) {
        return TouchPointConstants.AGENT_NAME_PREFIX + "." + name;
    }

    public static String touchPointFilterName(String filter) {
        return TouchPointConstants.TOUCH_POINT_FILTER_PREFIX + "." + filter;
    }

    public static String touchPointReceiverClassName(String type) {
        return String.format(TouchPointConstants.TOUCH_POINT_PKG + ".%sBroadcastReceiver", type);
    }

    public static String touchPointSubscriberClassName(String type) {
        return String.format(TouchPointConstants.TOUCH_POINT_PKG + ".%sSubscriber", type);
    }

    public static String touchPointContentProviderUri(String prefix, String objectName) {
        return String.format("content://%s/%s", prefix, objectName);
    }

}
