package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.arp.AgentRouteEntry;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<AgentRouteEntry> agentRouteEntries,
                          AIModelResponse.AgentAction action, String question);

}
