package com.universe.touchpoint.ai;

import com.universe.touchpoint.ai.models.Anthropic;
import com.universe.touchpoint.ai.models.OpenAI;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.config.ai.LangModelConfig;
import java.util.HashMap;
import java.util.Map;

public class AIModelFactory {

    private static AIModel<?, ?, ?, ?> model;
    private static final Object lock = new Object();

    private static final Map<AIModelType, Class<? extends AIModel<?, ?, ?, ?>>> modelMap = new HashMap<>();
    static {
        modelMap.put(AIModelType.OPEN_AI, OpenAI.class);
        modelMap.put(AIModelType.ANTHROPIC, Anthropic.class);
        modelMap.put(AIModelType.OPEN_VLA, OpenVLA.class);
    }

    /**
     * 根据提供的模型类型字符串创建相应的 AIModel 实例
     *
     * @param modelConfig 模型配置
     * @return 对应的 AIModel 实例
     */
    public static <C, REQ, CH> AIModel<?, REQ, C, CH> getModel(LangModelConfig modelConfig) {
        synchronized (lock) {
            if (model == null) {
                try {
                    // 获取模型类类型
                    Class<? extends AIModel<?, REQ, C, CH>> modelClass = (Class<? extends AIModel<?, REQ, C, CH>>) modelMap.get(modelConfig.getType());
                    if (modelClass == null) {
                        throw new IllegalArgumentException("Unknown model type: " + modelConfig.getType());
                    }
                    model = modelClass.getConstructor(LangModelConfig.class).newInstance(modelConfig);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error creating model for type: " + modelConfig.getType(), e);
                }
            }
            return (AIModel<?, REQ, C, CH>) model;
        }
    }

    public static <C, REQ, CHOICES> CHOICES callModel(REQ req, LangModelConfig modelConfig) {
        // 获取模型实例
        AIModel<?, REQ, C, CHOICES> model = getModel(modelConfig);
        // 创建聊天对话
        model.createCompletion();
        // 执行推理并获取choice，随机选择一个choice
        return model.predict(req);
    }

}
