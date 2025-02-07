package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.config.ActionConfig;

import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator {

    @Override
    public String generatePrompt(List<ActionConfig> actionMetaInfos, AgentAction action, String question) {
        return "";
    }

}
