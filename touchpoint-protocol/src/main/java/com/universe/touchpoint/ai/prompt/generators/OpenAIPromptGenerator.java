package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.meta.AgentActionMeta;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.ai.prompt.template.OpenAITemplate;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.utils.ClassUtils;
import java.util.List;
import javax.annotation.Nonnull;

public class OpenAIPromptGenerator implements PromptGenerator<String> {

    @Override
    public <I extends TouchPoint, O> String generatePrompt(@Nonnull List<AgentActionMeta> nextActions, AgentAction<I, O> action, String question) {
        StringBuilder toolList = new StringBuilder();
        StringBuilder agentNames = new StringBuilder();

        for (AgentActionMeta actionMetaInfo : nextActions) {
            toolList.append(actionMetaInfo.getName())
                    .append(": ")
                    .append(actionMetaInfo.getDesc()).append("\n");
            agentNames.append(actionMetaInfo.getName()).append(", ");
        }

        // Remove trailing comma and space from agentNames
        if (!agentNames.isEmpty()) {
            agentNames.setLength(agentNames.length() - 2);
        }

        StringBuilder actionBody = new StringBuilder();
        String finalSuffix = OpenAITemplate.SUFFIX.replace("{question}", question);
        if (action != null) {
            finalSuffix = finalSuffix.replace("{agent_scratchpad}", action.getThought());
            String actionName = action.getActionName();
            String actionInput = ClassUtils.getFieldValues(action.getInput());
            String observation = action.getOutput().toString();
            actionBody.append(finalSuffix)
                    .append("\nAction:")
                    .append(actionName)
                    .append("\nAction Input:")
                    .append(actionInput)
                    .append("\nObservation:")
                    .append(observation);
        }
        // Replace [{agent_names}] with the agent names
        String finalFormatInstructions = OpenAITemplate.FORMAT_INSTRUCTIONS.replace("{agent_names}", agentNames.toString());

        // Combine PREFIX, dynamically generated TOOL_LIST, FORMAT_INSTRUCTIONS, and SUFFIX
        return OpenAITemplate.PREFIX + toolList + finalFormatInstructions + actionBody;
    }

}
