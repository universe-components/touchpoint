package com.universe.touchpoint.socket;

public enum AgentSocketState {

    REDIRECT_ACTION_READY(100),
    PARTICIPANT_READY(101),
    GLOBAL_CONFIG_DISTRIBUTED(102),
    GLOBAL_CONFIG_READY(103),
    ACTION_GRAPH_DISTRIBUTED(104),
    CHANNEL_ESTABLISHED(105);

    private final int code;

    AgentSocketState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static AgentSocketState next(AgentSocketState currentState) {
        return switch (currentState) {
            case REDIRECT_ACTION_READY -> PARTICIPANT_READY;
            case PARTICIPANT_READY -> ACTION_GRAPH_DISTRIBUTED;
            case ACTION_GRAPH_DISTRIBUTED -> GLOBAL_CONFIG_READY;
            case GLOBAL_CONFIG_READY -> GLOBAL_CONFIG_DISTRIBUTED;
            case GLOBAL_CONFIG_DISTRIBUTED -> CHANNEL_ESTABLISHED;
            case CHANNEL_ESTABLISHED -> null;
        };
    }

}
