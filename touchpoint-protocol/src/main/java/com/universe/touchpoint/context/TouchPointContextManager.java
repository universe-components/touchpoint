package com.universe.touchpoint.context;

import android.content.Context;
import android.net.Uri;

import com.universe.touchpoint.TouchPointConstants;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class TouchPointContextManager {

    private static final Object lock = new Object();
    private final static Map<String, TouchPointContext> touchPointContextMap = new HashMap<>();

    public static TouchPointContext getTouchPointContext(String task) {
        if (!touchPointContextMap.containsKey(task)) {
            synchronized (lock) {
                if (!touchPointContextMap.containsKey(task)) {
                    touchPointContextMap.put(task, new TouchPointContext(task));
                }
            }
        }
        return touchPointContextMap.get(task);
    }

    public static <T extends TouchPoint> T generateTouchPoint(Class<T> tpClass, String name) {
        Context ctx = Agent.getContext();
        return generateTouchPoint(tpClass, name, ctx);
    }

    public static AgentAction<?, ?> generateTouchPoint(AgentAction<?, ?> action, String content) {
        Context ctx = Agent.getContext();
        TouchPointChannel<?> channel = TouchPointChannelManager.selectChannel(action.getMeta(), ctx);

        action.setHeader(new TouchPoint.Header(action.getMeta(), channel));
        action.getContext().setTaskContext(new TaskContext(content));

        return action;
    }

    public static <T> T generateTouchPoint(Class<T> tpClass, String name, Context context) {
        try {
            TouchPointChannel<?> channel = TouchPointChannelManager.defaultChannel(context);
            T selfTouchPoint = tpClass.getConstructor().newInstance();
            Class<?> touchPointClass = tpClass.getSuperclass();

            assert touchPointClass != null;
            Method setNameMethod = touchPointClass.getDeclaredMethod("setToAgent", String.class);
            setNameMethod.invoke(selfTouchPoint, name);

            Method setChannelMethod = touchPointClass.getDeclaredMethod("setChannel", TouchPointChannel.class);
            setChannelMethod.invoke(selfTouchPoint, channel);

            return selfTouchPoint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends TouchPoint> T fetchTouchPoint(String filter, Class<T> clazz) {
        String contentProviderUri = TouchPointHelper.touchPointContentProviderUri(
                TouchPointConstants.CONTENT_PROVIDER_PREFIX,
                filter);
        TouchPointContent touchPointContent = TouchPointContentFactory.createContent(
                Uri.parse(contentProviderUri),
                Agent.getContext());

        return touchPointContent.query(clazz);
    }

}
