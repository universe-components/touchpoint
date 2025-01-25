package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.ai.prompt.PromptGenerator;

import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator {

    @Override
    public String generatePrompt(List<AgentRouteEntry> agentRouteEntries, AgentAction action, String question) {
        return "";
    }

}
