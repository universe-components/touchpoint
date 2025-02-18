package com.universe.touchpoint.state;

public class TouchPointState {

    protected int code;
    protected String message;
    protected String ctxName;
    protected String redirectToAction;

    public TouchPointState(int code, String message) {
        this(code, message, null);
    }

    public TouchPointState(int code, String message, String action) {
        this(code, message, action, null);
    }

    public TouchPointState(int code, String message, String action, String ctxName) {
        this.code = code;
        this.message = message;
        this.redirectToAction = action;
        this.ctxName = ctxName;
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

    public void setCtxName(String ctxName) {
        this.ctxName = ctxName;
    }

    public String getCtxName() {
        return ctxName;
    }

    public void setRedirectToAction(String redirectToAction) {
        this.redirectToAction = redirectToAction;
    }

    public String getRedirectToAction() {
        return redirectToAction;
    }

}
