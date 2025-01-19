package com.universe.touchpoint.dispatcher.prompt.generators;

import com.universe.touchpoint.dispatcher.AgentRouteItem;
import com.universe.touchpoint.dispatcher.prompt.PromptGenerator;
import com.universe.touchpoint.dispatcher.prompt.template.OpenAITemplate;

import java.util.List;

public class OpenAIPromptGenerator implements PromptGenerator {

    @Override
    public String generatePrompt(List<AgentRouteItem> agentRouteItems, String question) {
        StringBuilder toolList = new StringBuilder();
        StringBuilder agentNames = new StringBuilder();

        for (AgentRouteItem item : agentRouteItems) {
            toolList.append(item.getToAgent().getName())
                    .append(": ")
                    .append(item.getToAgent().getDescription()).append("\n");
            agentNames.append(item.getToAgent().getName()).append(", ");
        }

        // Remove trailing comma and space from agentNames
        if (agentNames.length() > 0) {
            agentNames.setLength(agentNames.length() - 2);
        }

        // Replace {input} with the actual question
        String finalSuffix = OpenAITemplate.SUFFIX.replace("{input}", question);
        // Replace [{agent_names}] with the agent names
        String finalFormatInstructions = OpenAITemplate.FORMAT_INSTRUCTIONS.replace("{agent_names}", agentNames.toString());

        // Combine PREFIX, dynamically generated TOOL_LIST, FORMAT_INSTRUCTIONS, and SUFFIX
        return OpenAITemplate.PREFIX + toolList + finalFormatInstructions + finalSuffix;
    }

}
