package com.universe.touchpoint.dispatcher.prompt;

import com.universe.touchpoint.dispatcher.AgentRouteItem;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<AgentRouteItem> agentRouteItems, String question);

}
