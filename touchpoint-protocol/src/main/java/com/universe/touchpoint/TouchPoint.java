package com.universe.touchpoint;

import android.net.Uri;

import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;

public abstract class TouchPoint {

    public Header header;
    public String filter;
    public String content;
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

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public void setChannel(TouchPointChannel channel) {
        this.channel = channel;
    }

    public boolean finish() {
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

    public static class Header {

        public Header(String fromAgent, String toAgent) {
            this.fromAgent = fromAgent;
            this.toAgent = toAgent;
        }

        private final String fromAgent;
        private final String toAgent;

        public String getFromAgent() {
            return fromAgent;
        }

        public String getToAgent() {
            return toAgent;
        }

    }

}
