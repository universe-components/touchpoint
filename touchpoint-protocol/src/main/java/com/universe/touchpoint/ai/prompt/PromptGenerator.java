package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.context.TouchPoint;

import java.util.List;

public interface PromptGenerator {

    <I extends TouchPoint, O> String generatePrompt(List<AgentActionMetaInfo> taskActions, AgentAction<I, O> action, String question);

}
