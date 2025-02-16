package com.universe.touchpoint.state.enums;

import com.universe.touchpoint.rolemodel.Scope;

public enum TaskState {

    OK(200, Scope.ALL),
    NEED_REORDER_ACTION(300, Scope.ACTION_GRAPH),
    NEED_SWITCH_AI_MODEL(301, Scope.ACTION),
    NEED_SWITCH_ACTION(302, Scope.ACTION),
    NEED_CHECK_ACTION(401, Scope.ACTION),
    NEED_CHECK_ACTION_GRAPH(402, Scope.ACTION_GRAPH),
    NEED_CHECK_DATA(403, Scope.DATA);

    private final int code;
    private final Scope scope;

    TaskState(int code, Scope scope) {
        this.code = code;
        this.scope = scope;
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

    public Scope getScope() {
        return scope;
    }

}
