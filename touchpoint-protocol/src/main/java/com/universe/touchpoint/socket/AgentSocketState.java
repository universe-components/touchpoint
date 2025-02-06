package com.universe.touchpoint.socket;

public enum AgentSocketState {

    TASK_READY,
    PARTICIPANT_READY,
    GLOBAL_CONFIG_DISTRIBUTED,
    GLOBAL_CONFIG_READY,
    ACTION_GRAPH_DISTRIBUTED,
    CHANNEL_ESTABLISHED;

    public static AgentSocketState next(AgentSocketState currentState) {
        return switch (currentState) {
            case TASK_READY -> PARTICIPANT_READY;
            case PARTICIPANT_READY -> GLOBAL_CONFIG_DISTRIBUTED;
            case GLOBAL_CONFIG_DISTRIBUTED -> GLOBAL_CONFIG_READY;
            case GLOBAL_CONFIG_READY -> ACTION_GRAPH_DISTRIBUTED;
            case ACTION_GRAPH_DISTRIBUTED -> CHANNEL_ESTABLISHED;
            case CHANNEL_ESTABLISHED -> null;
        };
    }

}
