package com.universe.touchpoint;

import android.content.Context;
import android.net.Uri;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.transport.TouchPointChannelManager;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;

import java.lang.reflect.Method;

public class TouchPointContextManager {

    public static <T extends TouchPoint> T generateTouchPoint(Class<T> tpClass, String name) {
        Context ctx = Agent.getContext();
        return generateTouchPoint(tpClass, name, ctx);
    }

    public static <A extends TouchPoint> A generateTouchPoint(
            A action, String name, String content) {
        Context ctx = Agent.getContext();
        TouchPointChannel channel = TouchPointChannelManager.selectChannel(action, ctx);

        action.setHeader(new TouchPoint.Header(Agent.getName(), name, channel));
        action.setGoal(content);

        return action;
    }

    public static <T> T generateTouchPoint(Class<T> tpClass, String name, Context context) {
        try {
            TouchPointChannel channel = TouchPointChannelManager.defaultChannel(context);
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
