package com.universe.touchpoint.ai;

import com.universe.touchpoint.ai.models.Anthropic;
import com.universe.touchpoint.ai.models.OpenAI;

import java.util.HashMap;
import java.util.Map;

public class AIModelFactory {

    private static AIModel<?, ?, ?> model;
    private static final Object lock = new Object();

    private static final Map<AIModelType, Class<? extends AIModel<?, ?, ?>>> modelMap = new HashMap<>();
    static {
        modelMap.put(AIModelType.OPEN_AI, OpenAI.class);
        modelMap.put(AIModelType.ANTHROPIC, Anthropic.class);
    }

    /**
     * 根据提供的模型类型字符串创建相应的 AIModel 实例
     *
     * @param modelType 模型的字符串标识
     * @return 对应的 AIModel 实例
     */
    public static <C, R> AIModel<?, C, R> createModel(AIModelType modelType) {
        synchronized (lock) {
            if (model == null) {
                try {
                    // 获取模型类类型
                    Class<? extends AIModel<?, C, R>> modelClass = (Class<? extends AIModel<?, C, R>>) modelMap.get(modelType);
                    if (modelClass == null) {
                        throw new IllegalArgumentException("Unknown model type: " + modelType);
                    }
                    model = modelClass.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating model for type: " + modelType, e);
                }
            }
            return (AIModel<?, C, R>) model;
        }
    }

    public static <C, R> R callModel(String content, AIModelType modelType) {
        // 获取模型实例
        AIModel<?, C, R> model = createModel(modelType);
        // 创建聊天对话
        model.createCompletion(content);
        // 执行推理并获取choice，随机选择一个choice
        return model.selectChoice(model.predict());
    }

}
