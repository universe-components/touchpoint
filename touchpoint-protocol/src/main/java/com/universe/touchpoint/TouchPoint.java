package com.universe.touchpoint;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.state.TouchPointState;
import com.universe.touchpoint.state.enums.ActionState;
import com.universe.touchpoint.transport.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;

import java.lang.reflect.Field;

public abstract class TouchPoint {

    protected Header header = new Header();
    public String goal;
    protected TouchPointState state;

    protected TouchPoint() {
        this.state.setCode(ActionState.SUCCESS.getCode());
    }

    protected TouchPoint(String goal, Header header) {
        this.goal = goal;
        this.header = header;
        this.state.setCode(ActionState.SUCCESS.getCode());
    }

    public void setGoal(String goal) {
        this.goal = goal;
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

    public void setState(TouchPointState state) {
        this.state = state;
    }

    public TouchPointState getState() {
        return state;
    }

    @NonNull
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
            String contentProviderUri = TouchPointHelper.touchPointContentProviderUri(
                    TouchPointConstants.CONTENT_PROVIDER_PREFIX, header.fromAction.actionName());
            TouchPointContent touchPointContent = TouchPointContentFactory.createContent(Uri.parse(contentProviderUri), Agent.getContext());
            boolean rs = touchPointContent.insertOrUpdate(this);
            if (!rs) {
                throw new RuntimeException("insertOrUpdate failed");
            }
            header.channel.send(this);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Header {

        private AgentActionMetaInfo fromAction = null;
        private final String toAction = null;
        private transient TouchPointChannel channel;

        public Header() {
        }

        public Header(AgentActionMetaInfo fromAction) {
            this.fromAction = fromAction;
        }

        public Header(AgentActionMetaInfo fromAction, TouchPointChannel channel) {
            this.fromAction = fromAction;
            this.channel = channel;
        }

        public AgentActionMetaInfo getFromAction() {
            return fromAction;
        }

        public String getToAction() {
            return toAction;
        }

        public TouchPointChannel getChannel() {
            return channel;
        }

        public void setChannel(TouchPointChannel channel) {
            this.channel = channel;
        }

    }

}
