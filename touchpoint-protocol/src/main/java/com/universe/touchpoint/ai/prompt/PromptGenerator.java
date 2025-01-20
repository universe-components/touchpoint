package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.transport.AgentRouteItem;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<AgentRouteItem> agentRouteItems,
                          AIModelResponse.AgentAction action, String question);

}
