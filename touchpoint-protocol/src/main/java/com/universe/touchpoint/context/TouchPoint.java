package com.universe.touchpoint.context;

import com.universe.touchpoint.meta.AgentActionMeta;
import com.universe.touchpoint.transport.TouchPointChannel;
import java.lang.reflect.Field;
import javax.annotation.Nonnull;

public class TouchPoint {

    protected Header header;
    protected TouchPointContext context;
    protected TouchPointState state;

    public TouchPoint() {
        this.state.setCode(TaskState.OK.getCode());
    }

    protected TouchPoint(Header header, TouchPointContext context) {
        this.header = header;
        this.context = context;
        this.state.setCode(TaskState.OK.getCode());
    }

    public void setChannel(TouchPointChannel<?> channel) {
        this.header.setChannel(channel);
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public TouchPointContext getContext() {
        return context;
    }

    public void setState(TouchPointState state) {
        this.state = state;
    }

    public TouchPointState getState() {
        return state;
    }

    @Nonnull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Class<?> clazz = this.getClass(); // 当前类
        result.append(clazz.getSimpleName()).append(" {");

        // 遍历所有字段，包括父类的字段
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // 确保可以访问私有字段
                try {
                    // 获取字段名和字段值
                    String name = field.getName();
                    Object value = field.get(this);
                    result.append("\n  ").append(name).append(": ").append(value);
                } catch (IllegalAccessException e) {
                    result.append("\n  ").append(field.getName()).append(": [access denied]");
                }
            }
            clazz = clazz.getSuperclass(); // 获取父类
        }
        result.append("\n}");

        return result.toString();
    }

    public boolean finish() {
        try {
            header.channel.send(this);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Header {

        private AgentActionMeta fromAction = null;
        private AgentActionMeta toAction = null;
        private transient TouchPointChannel<?> channel;

        public Header() {
        }

        public Header(AgentActionMeta fromAction) {
            this.fromAction = fromAction;
        }

        public Header(AgentActionMeta fromAction, TouchPointChannel<?> channel) {
            this.fromAction = fromAction;
            this.channel = channel;
        }

        public AgentActionMeta getFromAction() {
            return fromAction;
        }

        public void setToAction(String toAction) {
            this.toAction = new AgentActionMeta(toAction);
        }

        public void setToAction(AgentActionMeta toAction) {
            this.toAction = toAction;
        }

        public AgentActionMeta getToAction() {
            return toAction;
        }

        public TouchPointChannel<?> getChannel() {
            return channel;
        }

        public void setChannel(TouchPointChannel<?> channel) {
            this.channel = channel;
        }

    }

}
