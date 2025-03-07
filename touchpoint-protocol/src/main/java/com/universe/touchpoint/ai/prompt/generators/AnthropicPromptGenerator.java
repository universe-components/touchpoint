package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import java.util.List;

public class AnthropicPromptGenerator implements PromptGenerator<String> {

    @Override
    public <I, O> String generatePrompt(List<AgentActionMeta> taskActions, AgentAction<I, O> action, String question) {
        return "";
    }

}
