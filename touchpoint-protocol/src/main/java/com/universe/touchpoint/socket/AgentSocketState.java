package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.role.RoleType;

public enum AgentSocketState {

    ACTION_READY(100, RoleType.MEMBER),
    ACTION_GRAPH_READY(101, RoleType.MEMBER),
    PARTICIPANT_READY(102, RoleType.OWNER),
    GLOBAL_CONFIG_DISTRIBUTED(103, RoleType.MEMBER),
    GLOBAL_CONFIG_READY(104, RoleType.OWNER),
    ACTION_GRAPH_DISTRIBUTED(105, RoleType.MEMBER),
    CHANNEL_ESTABLISHED(106, RoleType.OWNER);

    private final int code;
    private final RoleType role;

    AgentSocketState(int code, RoleType role) {
        this.code = code;
        this.role = role;
    }

    public int getCode() {
        return code;
    }

    public RoleType getRole() {
        return role;
    }

    public static AgentSocketState next(AgentSocketState currentState) {
        return switch (currentState) {
            case ACTION_GRAPH_READY, PARTICIPANT_READY -> GLOBAL_CONFIG_DISTRIBUTED;
            case ACTION_READY -> PARTICIPANT_READY;
            case GLOBAL_CONFIG_DISTRIBUTED -> GLOBAL_CONFIG_READY;
            case GLOBAL_CONFIG_READY -> ACTION_GRAPH_DISTRIBUTED;
            case ACTION_GRAPH_DISTRIBUTED -> CHANNEL_ESTABLISHED;
            case CHANNEL_ESTABLISHED -> null;
        };
    }

}
