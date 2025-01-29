package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.config.ActionConfig;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<ActionConfig> taskActions, AgentAction action, String question);

}
