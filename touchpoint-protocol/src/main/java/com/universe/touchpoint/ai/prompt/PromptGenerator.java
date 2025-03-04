package com.universe.touchpoint.ai.prompt;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.context.TouchPoint;
import java.util.List;

public interface PromptGenerator<Prompt> {

    <I extends TouchPoint, O> Prompt generatePrompt(List<AgentActionMeta> taskActions, AgentAction<I, O> action, String question);

}
