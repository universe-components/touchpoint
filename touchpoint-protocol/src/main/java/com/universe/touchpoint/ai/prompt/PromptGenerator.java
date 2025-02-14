package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;

import java.util.List;

public interface PromptGenerator {

    String generatePrompt(List<AgentActionMetaInfo> taskActions, AgentAction<?> action, String question);

}
