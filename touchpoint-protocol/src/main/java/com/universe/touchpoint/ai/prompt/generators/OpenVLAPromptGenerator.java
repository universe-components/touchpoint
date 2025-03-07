package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.meta.data.AgentActionMeta;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import java.util.List;

public class OpenVLAPromptGenerator implements PromptGenerator<OpenVLA.ActionRequest> {

    @Override
    public <I, O> OpenVLA.ActionRequest generatePrompt(List<AgentActionMeta> taskActions, AgentAction<I, O> action, String question) {
        return (OpenVLA.ActionRequest) action.getOutput();
    }

}
