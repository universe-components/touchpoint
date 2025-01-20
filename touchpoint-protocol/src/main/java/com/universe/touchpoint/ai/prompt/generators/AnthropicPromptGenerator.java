package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.router.AgentRouteItem;
import com.universe.touchpoint.ai.prompt.PromptGenerator;

import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator {

    @Override
    public String generatePrompt(List<AgentRouteItem> agentRouteItems, AIModelResponse.AgentAction action, String question) {
        return "";
    }

}
