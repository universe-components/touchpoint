package com.universe.touchpoint.state;

public abstract class TouchPointState {

    protected int code;
    protected String message;
    protected String action;

    public TouchPointState(int code, String message) {
        this(code, message, null);
    }

    public TouchPointState(int code, String message, String action) {
        this.code = code;
        this.message = message;
        this.action = action;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

}
