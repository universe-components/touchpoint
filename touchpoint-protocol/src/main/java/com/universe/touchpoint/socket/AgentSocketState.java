package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.role.RoleType;

public enum AgentSocketState {

    ACTION_READY(100, RoleType.MEMBER),
    COORDINATOR_ACTION_GRAPH_READY(101, RoleType.MEMBER),
    ACTION_GRAPH_READY(102, RoleType.OWNER),
    GLOBAL_CONFIG_READY(103, RoleType.MEMBER),
    REQUEST_GRAPH_READY(104, RoleType.OWNER),
    ROUTER_READY(105, RoleType.MEMBER),
    NEGOTIATION_CONCLUDED(106, RoleType.OWNER);

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
            case COORDINATOR_ACTION_GRAPH_READY, ACTION_GRAPH_READY -> GLOBAL_CONFIG_READY;
            case ACTION_READY -> ACTION_GRAPH_READY;
            case GLOBAL_CONFIG_READY -> REQUEST_GRAPH_READY;
            case REQUEST_GRAPH_READY -> ROUTER_READY;
            case ROUTER_READY -> NEGOTIATION_CONCLUDED;
            case NEGOTIATION_CONCLUDED -> null;
        };
    }

}
