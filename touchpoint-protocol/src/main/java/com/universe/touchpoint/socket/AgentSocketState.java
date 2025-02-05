package com.universe.touchpoint.socket;

public enum AgentSocketState {

    TASK_READY,
    PARTICIPANT_READY,
    TRANSPORT_DISTRIBUTED,
    AI_MODEL_DISTRIBUTED;

    public static AgentSocketState next(AgentSocketState currentState) {
        return switch (currentState) {
            case TASK_READY -> PARTICIPANT_READY;
            case PARTICIPANT_READY -> TRANSPORT_DISTRIBUTED;
            case TRANSPORT_DISTRIBUTED, AI_MODEL_DISTRIBUTED -> null;
        };
    }

}
