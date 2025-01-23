package com.universe.touchpoint.ai.prompt.generators;

import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.ai.prompt.template.OpenAITemplate;

import java.util.List;

public class OpenAIPromptGenerator implements PromptGenerator {


    @Override
    public String generatePrompt(List<AgentRouteEntry> agentRouteEntries,
                                 AIModelResponse.AgentAction action, String question) {
        StringBuilder toolList = new StringBuilder();
        StringBuilder agentNames = new StringBuilder();

        for (AgentRouteEntry item : agentRouteEntries) {
            toolList.append(item.getToAgent().getName())
                    .append(": ")
                    .append(item.getToAgent().getDescription()).append("\n");
            agentNames.append(item.getToAgent().getName()).append(", ");
        }

        // Remove trailing comma and space from agentNames
        if (agentNames.length() > 0) {
            agentNames.setLength(agentNames.length() - 2);
        }

        StringBuilder actionBody = new StringBuilder();
        String finalSuffix = OpenAITemplate.SUFFIX.replace("{input}", question);
        if (action != null) {
            finalSuffix = finalSuffix.replace("{agent_scratchpad}", action.getThought());
            actionBody.append(finalSuffix)
                    .append("\nAction:")
                    .append(action.getAction())
                    .append("\nAction Input:")
                    .append(action.getActionInput())
                    .append("\nObservation:")
                    .append(action.getObservation());
        }
        // Replace [{agent_names}] with the agent names
        String finalFormatInstructions = OpenAITemplate.FORMAT_INSTRUCTIONS.replace("{agent_names}", agentNames.toString());

        // Combine PREFIX, dynamically generated TOOL_LIST, FORMAT_INSTRUCTIONS, and SUFFIX
        return OpenAITemplate.PREFIX + toolList + finalFormatInstructions + actionBody;
    }

}
