package com.universe.touchpoint.agent;

import com.universe.touchpoint.agent.decoder.ActionInstructionDecoder;
import com.universe.touchpoint.agent.decoder.DefaultModelOutputDecoder;

import java.util.HashMap;
import java.util.Map;

public class ModelOutputDecoderSelector {

    private static final Map<ActionType, ModelOutputDecoder<?, ?>> actionDecoderMap = new HashMap<>();
    static {
        actionDecoderMap.put(ActionType.INPUT, new DefaultModelOutputDecoder<>());
        actionDecoderMap.put(ActionType.SENSOR, new ActionInstructionDecoder());
    }

    public static ModelOutputDecoder<?, ?> selectParamsDecoder(ActionType type) {
        return actionDecoderMap.getOrDefault(type, new DefaultModelOutputDecoder<>());
    }

}
