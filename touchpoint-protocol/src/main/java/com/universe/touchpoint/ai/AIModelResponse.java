package com.universe.touchpoint.ai;

import androidx.annotation.NonNull;

import com.universe.touchpoint.TouchPoint;

public class AIModelResponse {

    public static class AgentAction extends TouchPoint {
        private String action;
        private String actionInput;
        private String thought;
        private String observation;

        public AgentAction(String action, String actionInput, String thought) {
            this.action = action;
            this.actionInput = actionInput;
            this.thought = thought;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getActionInput() {
            return actionInput;
        }

        public void setActionInput(String actionInput) {
            this.actionInput = actionInput;
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

        @NonNull
        @Override
        public String toString() {
            return "AgentAction{" +
                    "action='" + action + '\'' +
                    ", actionInput='" + actionInput + '\'' +
                    ", thought='" + thought + '\'' +
                    ", observation='" + observation + '\'' +
                    '}';
        }
    }

    // 公共内部类 AgentFinish
    public static class AgentFinish {
        private String output;

        public AgentFinish(String output) {
            this.output = output;
        }

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        @NonNull
        @Override
        public String toString() {
            return "AgentFinish{" +
                    "output='" + output + '\'' +
                    '}';
        }
    }

}
