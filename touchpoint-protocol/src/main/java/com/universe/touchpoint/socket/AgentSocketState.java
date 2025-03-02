package com.universe.touchpoint.socket;

import com.universe.touchpoint.annotations.role.ActionRole;

public enum AgentSocketState {

    ACTION_READY(100, ActionRole.COORDINATOR),
    ACTION_GRAPH_READY(101, ActionRole.COORDINATOR),
    PARTICIPANT_READY(102, ActionRole.PROPOSER),
    GLOBAL_CONFIG_DISTRIBUTED(103, ActionRole.PARTICIPANT),
    GLOBAL_CONFIG_READY(104, ActionRole.PROPOSER),
    ACTION_GRAPH_DISTRIBUTED(105, ActionRole.PARTICIPANT),
    CHANNEL_ESTABLISHED(106, ActionRole.PROPOSER);

    private final int code;
    private final ActionRole role;

    AgentSocketState(int code, ActionRole role) {
        this.code = code;
        this.role = role;
    }

    public int getCode() {
        return code;
    }

    public ActionRole getRole() {
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
