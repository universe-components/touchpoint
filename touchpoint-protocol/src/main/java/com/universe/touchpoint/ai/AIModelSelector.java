package com.universe.touchpoint.ai;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.Model;

public class AIModelSelector {

    public static AIModelType selectModel(String input, AgentAction action) {
        Model modelFromAction = action.getMeta().model();
        if (modelFromAction == null) {
            // 如果 action 中的模型为空，再检查 Agent 的模型
            Model model = Agent.getModel();
            if (model == null) {
                // 如果 Agent.getModel() 也为空，返回默认模型 OPEN_AI
                return AIModelType.OPEN_AI;
            }

            // 根据 Agent.getModel() 返回相应的 AIModelType
            return switch (model) {
                case GPT_3_5, o1, GPT_4 -> AIModelType.OPEN_AI;
                case ClAUDE_3_5_SONNET -> AIModelType.ANTHROPIC;
            };
        }

        // 如果 action.getMeta().getModel() 不为空，根据该值返回模型类型
        return switch (modelFromAction) {
            case GPT_3_5, o1, GPT_4 -> AIModelType.OPEN_AI;
            case ClAUDE_3_5_SONNET -> AIModelType.ANTHROPIC;
        };
    }

}
