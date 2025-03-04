package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.context.TouchPoint;
import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator<String> {

    @Override
    public <I extends TouchPoint, O> String generatePrompt(List<AgentActionMeta> taskActions, AgentAction<I, O> action, String question) {
        return "";
    }

}
