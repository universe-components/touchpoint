package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;

public class AgentAction<I extends TouchPoint, O extends TouchPoint> extends TouchPoint {

    private String action;
    private String thought;
    private O output;
    private I input;
    private AgentActionMetaInfo meta;

    public AgentAction(String goal, String task, Header header) {
        super(goal, task, header);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public O getOutput() {
        return output;
    }

    public void setOutput(O output) {
        this.output = output;
    }

    public I getInput() {
        return input;
    }

    public void setInput(I input) {
        this.input = input;
    }

    public void setMeta(AgentActionMetaInfo meta) {
        this.meta = meta;
    }

    public AgentActionMetaInfo getMeta() {
        return meta;
    }

    //        @NonNull
//        @Override
//        public String toString() {
//            return "AgentAction{" +
//                    "action='" + action + '\'' +
//                    ", actionInput='" + actionInput + '\'' +
//                    ", thought='" + thought + '\'' +
//                    ", observation='" + observation + '\'' +
//                    '}';
//        }

}
