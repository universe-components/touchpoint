package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import java.util.List;

public interface PromptGenerator<Prompt> {

    <I, O> Prompt generatePrompt(List<AgentActionMeta> taskActions, AgentAction<I, O> action, String question);

}
