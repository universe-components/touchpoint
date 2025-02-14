package com.universe.touchpoint.state.enums;

public enum ActionState {

    NEED_REORDER_ACTION(100),
    NEED_SWITCH_ACTION(101),
    NEED_REMOVE_ACTION(102),
    SUCCESS(200),
    NEED_SUPERVISOR_CHECKING(301);

    private final int code;

    ActionState(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}
