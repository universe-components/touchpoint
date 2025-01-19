package com.universe.touchpoint.dispatcher.prompt;

import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.dispatcher.prompt.generators.AnthropicPromptGenerator;
import com.universe.touchpoint.dispatcher.prompt.generators.OpenAIPromptGenerator;

import java.util.HashMap;
import java.util.Map;

public class PromptBuilder {

    private static PromptGenerator promptGenerator;
    private static final Object lock = new Object();

    private static final Map<AIModelType, Class<? extends PromptGenerator>> modelGeneratorMap = new HashMap<>();
    static {
        modelGeneratorMap.put(AIModelType.OPEN_AI, OpenAIPromptGenerator.class);
        modelGeneratorMap.put(AIModelType.ANTHROPIC, AnthropicPromptGenerator.class);
    }

    public static PromptGenerator createPromptGenerator(AIModelType modelType) {
        synchronized (lock) {
            if (promptGenerator == null) {
                try {

                    // 获取模型类类型
                    Class<? extends PromptGenerator> modelGeneratorClass = modelGeneratorMap.get(modelType);
                    if (modelGeneratorClass == null) {
                        throw new IllegalArgumentException("Unknown model type: " + modelType);
                    }
                    promptGenerator = modelGeneratorClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating prompt generator for type: " + modelType, e);
                }
            }
            return promptGenerator;
        }
    }

}
