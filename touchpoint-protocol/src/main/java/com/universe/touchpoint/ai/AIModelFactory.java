package com.universe.touchpoint.ai;

import com.universe.touchpoint.ai.models.Anthropic;
import com.universe.touchpoint.ai.models.OpenAI;
import com.universe.touchpoint.config.ai.AIModelConfig;

import java.util.HashMap;
import java.util.List;
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
     * @param modelConfig 模型配置
     * @return 对应的 AIModel 实例
     */
    public static <C, CH> AIModel<?, C, CH> createModel(AIModelConfig modelConfig) {
        synchronized (lock) {
            if (model == null) {
                try {
                    // 获取模型类类型
                    Class<? extends AIModel<?, C, CH>> modelClass = (Class<? extends AIModel<?, C, CH>>) modelMap.get(modelConfig.getType());
                    if (modelClass == null) {
                        throw new IllegalArgumentException("Unknown model type: " + modelConfig.getType());
                    }
                    model = modelClass.getConstructor(AIModelConfig.class).newInstance(modelConfig);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating model for type: " + modelConfig.getType(), e);
                }
            }
            return (AIModel<?, C, CH>) model;
        }
    }

    public static <C, CH> Map<C, List<CH>> callModel(String content, AIModelConfig modelConfig) {
        // 获取模型实例
        AIModel<?, C, CH> model = createModel(modelConfig);
        // 创建聊天对话
        model.createCompletion(content);
        // 执行推理并获取choice，随机选择一个choice
        return model.predict();
    }

}
