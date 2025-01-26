package com.universe.touchpoint.ai;

import com.universe.touchpoint.AgentBuilder;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.annotations.AIModel;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Model;

public class AIModelSelector {

    public static AIModelConfig selectModel(String input, AgentAction action) {
        // 首先检查 action 中的模型
        Model modelFromAction = action.getMeta().model().getModel();
        if (modelFromAction != null) {
            // 如果 action 中有模型，则直接使用该模型
            return new AIModelConfig(
                    modelFromAction,
                    action.getMeta().model().getTemperature(),
                    modelFromAction == Model.ClAUDE_3_5_SONNET ? AIModelType.ANTHROPIC : AIModelType.OPEN_AI
            );
        }

        // 如果 action 中没有模型，再检查 Agent 的模型
        Model model = (Model) Agent.getProperty("model", AIModel.class);
        if (model != null) {
            float temperature = (float) Agent.getProperty("temperature", AIModel.class);
            // 根据 Agent.getModel() 返回相应的 AIModelType
            return switch (model) {
                case GPT_3_5, o1, GPT_4, NONE ->
                        new AIModelConfig(model, temperature, AIModelType.OPEN_AI);
                case ClAUDE_3_5_SONNET ->
                        new AIModelConfig(model, action.getMeta().model().getTemperature(), AIModelType.ANTHROPIC);
            };
        }

        // 如果 Agent.getModel() 也为空，再检查 AgentBuilder 中的配置
        Model modelFromBuilder = AgentBuilder.getBuilder().getConfig().getModelConfig().getModel();
        if (modelFromBuilder != null) {
            float temperatureFromBuilder = AgentBuilder.getBuilder().getConfig().getModelConfig().getTemperature();
            // 如果 modelFromBuilder 有值，则使用它来创建 AIModelConfig
            return new AIModelConfig(
                    modelFromBuilder,
                    temperatureFromBuilder,
                    modelFromBuilder == Model.ClAUDE_3_5_SONNET ? AIModelType.ANTHROPIC : AIModelType.OPEN_AI
            );
        }

        // 如果都没有模型，则返回默认模型 OPEN_AI
        return new AIModelConfig(Model.o1, 0.0f, AIModelType.OPEN_AI);
    }

}
