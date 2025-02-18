package com.universe.touchpoint.socket;

public enum AgentSocketState {

    PARTICIPANT_READY,
    GLOBAL_CONFIG_DISTRIBUTED,
    GLOBAL_CONFIG_READY,
    ACTION_GRAPH_DISTRIBUTED,
    ROLE_EXECUTOR_DISTRIBUTED,
    ROLE_EXECUTOR_MERGED,
    CHANNEL_ESTABLISHED;

    public static AgentSocketState next(AgentSocketState currentState) {
        return switch (currentState) {
            case PARTICIPANT_READY -> GLOBAL_CONFIG_DISTRIBUTED;
            case GLOBAL_CONFIG_DISTRIBUTED -> GLOBAL_CONFIG_READY;
            case GLOBAL_CONFIG_READY -> ACTION_GRAPH_DISTRIBUTED;
            case ACTION_GRAPH_DISTRIBUTED -> ROLE_EXECUTOR_DISTRIBUTED;
            case ROLE_EXECUTOR_DISTRIBUTED -> ROLE_EXECUTOR_MERGED;
            case ROLE_EXECUTOR_MERGED -> CHANNEL_ESTABLISHED;
            case CHANNEL_ESTABLISHED -> null;
        };
    }

}
