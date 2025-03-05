package com.universe.touchpoint.agent;

import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.context.TouchPoint;

public class AgentFinish<O> extends TouchPoint {

    private O output;
    private AgentActionMeta meta;

    public AgentFinish(O output, AgentActionMeta meta) {
        this.output = output;
        this.meta = meta;
    }

    public O getOutput() {
        return output;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public AgentActionMeta getMeta() {
        return meta;
    }

    public void setMeta(AgentActionMeta meta) {
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
