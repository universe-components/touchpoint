package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.router.AgentRouteEntry;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<AgentRouteEntry> agentRouteEntries, AgentAction action, String question);

}
