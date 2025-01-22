package com.universe.touchpoint;

import android.net.Uri;

import com.universe.touchpoint.channel.TouchPointChannel;
import com.universe.touchpoint.helper.TouchPointHelper;
import com.universe.touchpoint.provider.TouchPointContent;
import com.universe.touchpoint.provider.TouchPointContentFactory;

public abstract class TouchPoint {

    private Header header = new Header();
    public String content;

    protected TouchPoint() {
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannel(TouchPointChannel channel) {
        this.header.setChannel(channel);
    }

    public void setToAgent(String agent) {
        this.header.setToAgent(agent);
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return header;
    }

    public boolean finish() {
        try {
            String contentProviderUri = TouchPointHelper.touchPointContentProviderUri(
                    TouchPointConstants.CONTENT_PROVIDER_PREFIX, header.toAgent);
            TouchPointContent touchPointContent = TouchPointContentFactory.createContent(Uri.parse(contentProviderUri), TouchPointContext.getAgentContext());
            boolean rs = touchPointContent.insertOrUpdate(this);
            if (!rs) {
                throw new RuntimeException("insertOrUpdate failed");
            }
            return header.channel.send(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static class Header {

        private String fromAgent = null;
        private String toAgent = null;
        private transient TouchPointChannel channel;

        public Header() {
        }

        public Header(String fromAgent, String toAgent, TouchPointChannel channel) {
            this.fromAgent = fromAgent;
            this.toAgent = toAgent;
            this.channel = channel;
        }

        public String getFromAgent() {
            return fromAgent;
        }

        public String getToAgent() {
            return toAgent;
        }

        public void setToAgent(String toAgent) {
            this.toAgent = toAgent;
        }

        public TouchPointChannel getChannel() {
            return channel;
        }

        public void setChannel(TouchPointChannel channel) {
            this.channel = channel;
        }

    }

}
