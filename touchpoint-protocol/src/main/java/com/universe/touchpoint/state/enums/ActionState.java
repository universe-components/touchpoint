package com.universe.touchpoint.state.enums;

public enum ActionState {

    NEED_REORDER_ACTION(100),
    NEED_SUPERVISOR_CHECKING(101);

    private final int code;

    ActionState(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}
