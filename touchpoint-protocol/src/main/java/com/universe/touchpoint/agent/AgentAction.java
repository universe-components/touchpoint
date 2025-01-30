package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;

public class AgentAction extends TouchPoint {

    private String action;
    private String thought;
    private String observation;
    private TouchPoint actionInput;
    private AgentActionMetaInfo meta;

    public AgentAction(String action, TouchPoint actionInput, String thought, AgentActionMetaInfo meta) {
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

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public TouchPoint getActionInput() {
        return actionInput;
    }

    public void setActionInput(TouchPoint actionInput) {
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
