package com.universe.touchpoint.context;

public enum TaskState {

    OK(200),
    NEED_REORDER_ACTION(300),
    NEED_SWITCH_LANG_MODEL(301),
    NEED_SWITCH_VISION_MODEL(302),
    NEED_SWITCH_VISION_LANG_MODEL(303),
    NEED_SWITCH_TRANSPORT(304),
    NEED_SWITCH_ACTION(305),
    NEED_CHECK_ACTION(401),
    NEED_CHECK_ACTION_GRAPH(402),
    NEED_CHECK_DATA(403);

    private final int code;

    TaskState(int code) {
        this.code = code;
    }

    public static TaskState getState(int code) {
        for (TaskState taskState : TaskState.values()) {
            if (taskState.getCode() == code) {
                return taskState;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

}
