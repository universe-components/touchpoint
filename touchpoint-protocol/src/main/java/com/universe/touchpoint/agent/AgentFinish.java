package com.universe.touchpoint.agent;

import com.universe.touchpoint.TouchPoint;

public class AgentFinish extends TouchPoint {
    private String output;

    public AgentFinish(String output) {
        super();
        this.output = output;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

//        @NonNull
//        @Override
//        public String toString() {
//            return "AgentFinish{" +
//                    "output='" + output + '\'' +
//                    '}';
//        }

}
