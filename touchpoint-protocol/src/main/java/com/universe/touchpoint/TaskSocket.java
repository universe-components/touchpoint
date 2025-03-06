package com.universe.touchpoint;

public class TaskSocket {

    protected final String task;

    public TaskSocket(String task) {
        this.task = task;
    }

    public String send(String goal) {
        return send(goal, null, null);
    }

    public <T extends TouchPoint> String send(String goal, T params) {
        return send(goal, params, null);
    }

    public void send(String goal, TaskCallbackListener callbackListener) {
        send(goal, null, callbackListener);
    }

    public <T extends TouchPoint> String send(String goal, T params, TaskCallbackListener callbackListener) {
        return Dispatcher.dispatch(goal, task, params, callbackListener);
    }

    public static abstract class TaskCallbackListener {

        public abstract <T> void onSuccess(T result);

    }

}
