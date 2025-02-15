package com.universe.touchpoint.state.enums;

public enum TaskState {

    OK(200),
    NEED_REORDER_ACTION(300),
    NEED_SWITCH_AI_MODEL(301),
    NEED_SWITCH_ACTION(302),
    NEED_CHANGE_DATA(303),
    NEED_CHECK_ACTION(401),
    NEED_CHECK_ACTION_GRAPH(402),
    NEED_CHECK_DATA(403);

    private final int code;

    TaskState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
