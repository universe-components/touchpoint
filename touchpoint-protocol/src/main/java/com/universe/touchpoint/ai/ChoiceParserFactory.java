package com.universe.touchpoint.ai;

import com.universe.touchpoint.ai.parsers.AnthropicChoiceParser;
import com.universe.touchpoint.ai.parsers.OpenAIChoiceParser;

import java.util.HashMap;
import java.util.Map;

public class ChoiceParserFactory {

    private static ChoiceParser<?> choiceParser;
    private static final Object lock = new Object();

    private static final Map<AIModelType, Class<? extends ChoiceParser<?>>> choiceParserMap = new HashMap<>();
    static {
        choiceParserMap.put(AIModelType.OPEN_AI, OpenAIChoiceParser.class);
        choiceParserMap.put(AIModelType.ANTHROPIC, AnthropicChoiceParser.class);
    }

    public static <C> ChoiceParser<C> selectParser(AIModelType modelType) {
        synchronized (lock) {
            if (choiceParser == null) {
                try {
                    // 获取模型类类型
                    Class<? extends ChoiceParser<?>> choiceParserClass = choiceParserMap.get(modelType);
                    if (choiceParserClass == null) {
                        throw new IllegalArgumentException("Unknown model type: " + modelType);
                    }
                    choiceParser = choiceParserClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating choice parser for type: " + modelType, e);
                }
            }
            return (ChoiceParser<C>) choiceParser;
        }
    }

}
