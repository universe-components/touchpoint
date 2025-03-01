package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.models.OpenVLA;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.context.TouchPoint;
import java.util.List;

public class OpenVLAPromptGenerator implements PromptGenerator<OpenVLA.ActionRequest> {

    @Override
    public <I extends TouchPoint, O> OpenVLA.ActionRequest generatePrompt(List<AgentActionMetaInfo> taskActions, AgentAction<I, O> action, String question) {
        return (OpenVLA.ActionRequest) action.getOutput();
    }

}
