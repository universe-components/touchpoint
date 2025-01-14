package com.universe.touchpoint;

import android.net.Uri;
import android.util.Log;

import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;

import java.lang.reflect.Field;

public abstract class TouchPoint {

    public String filter;
    public transient TouchPointChannel channel;

    protected TouchPoint() {
    }

    protected TouchPoint(String filter) {
        this.filter = filter;
    }

    protected TouchPoint(String filter, TouchPointChannel channel) {
        this.filter = filter;
        this.channel = channel;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setChannel(TouchPointChannel channel) {
        this.channel = channel;
    }

    public boolean finish() throws IllegalAccessException {
        // 获取子类实例的类对象
        Class<?> clazz = this.getClass();

        // 获取子类的所有字段
        Field[] fields = clazz.getDeclaredFields();

        // 打印字段名和字段值
        for (Field field : fields) {
            field.setAccessible(true); // 设置私有字段可访问
            Object value = field.get(this); // 获取字段的值
            Log.i("MemberVariable", field.getName() + ": " + value);
        }

        try {
            String contentProviderUri = TouchPointHelper.touchPointContentProviderUri(
                    TouchPointConstants.CONTENT_PROVIDER_PREFIX, filter);
            TouchPointContent touchPointContent = TouchPointContentFactory.createContent(Uri.parse(contentProviderUri), TouchPointContext.getAgentContext());
            boolean rs = touchPointContent.insertOrUpdate(this);
            if (!rs) {
                throw new RuntimeException("insertOrUpdate failed");
            }
            return channel.send(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
