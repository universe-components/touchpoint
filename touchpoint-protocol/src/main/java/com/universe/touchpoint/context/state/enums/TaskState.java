package com.universe.touchpoint.context.state.enums;

import com.universe.touchpoint.rolemodel.RoleScope;

public enum TaskState {

    OK(200, RoleScope.ALL),
    NEED_REORDER_ACTION(300, RoleScope.ACTION_GRAPH),
    NEED_SWITCH_LANG_MODEL(301, RoleScope.ACTION),
    NEED_SWITCH_VISION_MODEL(302, RoleScope.ACTION),
    NEED_SWITCH_VISION_LANG_MODEL(303, RoleScope.ACTION),
    NEED_SWITCH_TRANSPORT(304, RoleScope.ACTION),
    NEED_SWITCH_ACTION(305, RoleScope.ACTION),
    NEED_CHECK_ACTION(401, RoleScope.ACTION),
    NEED_CHECK_ACTION_GRAPH(402, RoleScope.ACTION_GRAPH),
    NEED_CHECK_DATA(403, RoleScope.DATA);

    private final int code;
    private final RoleScope scope;

    TaskState(int code, RoleScope scope) {
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

    public RoleScope getScope() {
        return scope;
    }

}
