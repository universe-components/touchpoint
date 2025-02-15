package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;

public class AgentAction<I extends TouchPoint, O extends TouchPoint> extends TouchPoint {

    private String action;
    private String thought;
    private O output;
    private I actionInput;
    private AgentActionMetaInfo meta;

    public AgentAction(String goal, Header header) {
        super(goal, header);
    }

    public AgentAction(String action, I actionInput, String thought, AgentActionMetaInfo meta) {
        super();
        this.action = action;
        this.actionInput = actionInput;
        this.thought = thought;
        this.meta = meta;
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

    public I getActionInput() {
        return actionInput;
    }

    public void setActionInput(I actionInput) {
        this.actionInput = actionInput;
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
