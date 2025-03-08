package com.universe.touchpoint.agent;

import com.universe.touchpoint.agent.decoder.ActionInstructionDecoder;
import com.universe.touchpoint.agent.decoder.DefaultModelOutputDecoder;
import java.util.HashMap;
import java.util.Map;

public class AIModelOutputDecoderSelector {

    private static final Map<ActionType, AIModelOutputDecoder<?, ?>> actionDecoderMap = new HashMap<>();
    static {
        actionDecoderMap.put(ActionType.CHAT, new DefaultModelOutputDecoder<>());
        actionDecoderMap.put(ActionType.ACT, new ActionInstructionDecoder());
    }

    public static AIModelOutputDecoder<?, ?> selectParamsDecoder(ActionType type) {
        return actionDecoderMap.getOrDefault(type, new DefaultModelOutputDecoder<>());
    }

}
