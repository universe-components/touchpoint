package com.universe.touchpoint.agent;

import com.universe.touchpoint.context.TouchPoint;

public class AgentFinish extends TouchPoint {

    private String output;
    private AgentActionMetaInfo meta;

    public AgentFinish(String output, AgentActionMetaInfo meta) {
        this.output = output;
        this.meta = meta;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
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
