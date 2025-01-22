package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.arp.AgentRouteEntry;
import com.universe.touchpoint.ai.prompt.PromptGenerator;

import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator {

    @Override
    public String generatePrompt(List<AgentRouteEntry> agentRouteEntries, AIModelResponse.AgentAction action, String question) {
        return "";
    }

}
