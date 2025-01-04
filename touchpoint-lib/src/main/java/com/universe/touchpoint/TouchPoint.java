package com.universe.touchpoint;

import android.util.Log;

import com.universe.touchpoint.channel.TouchPointChannel;

import java.lang.reflect.Field;

public abstract class TouchPoint {

    public String name;
    public transient TouchPointChannel channel;

    protected TouchPoint() {
    }

    protected TouchPoint(String name) {
        this.name = name;
    }

    protected TouchPoint(String name, TouchPointChannel channel) {
        this.name = name;
        this.channel = channel;
    }

    public void setName(String name) {
        this.name = name;
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
            return channel.send(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
