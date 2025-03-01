package com.universe.touchpoint.agent;

import com.universe.touchpoint.context.TouchPoint;

public class AgentFinish<O> extends TouchPoint {

    private O output;
    private AgentActionMetaInfo meta;

    public AgentFinish(O output, AgentActionMetaInfo meta) {
        this.output = output;
        this.meta = meta;
    }

    public O getOutput() {
        return output;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public AgentActionMetaInfo getMeta() {
        return meta;
    }

    public void setMeta(AgentActionMetaInfo meta) {
        this.meta = meta;
    }

//        @NonNull
//        @Override
//        public String toString() {
//            return "AgentFinish{" +
//                    "output='" + output + '\'' +
//                    '}';
//        }

}
