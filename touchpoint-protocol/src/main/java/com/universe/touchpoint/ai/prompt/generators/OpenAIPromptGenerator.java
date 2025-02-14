package com.universe.touchpoint.ai.prompt.generators;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.ai.prompt.template.OpenAITemplate;
import com.universe.touchpoint.utils.ClassUtils;

import java.util.List;

public class OpenAIPromptGenerator implements PromptGenerator {

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @Override
    public String generatePrompt(@NonNull List<AgentActionMetaInfo> actionMetaInfoList, AgentAction<?> action, String question) {
        StringBuilder toolList = new StringBuilder();
        StringBuilder agentNames = new StringBuilder();

        for (AgentActionMetaInfo actionMetaInfo : actionMetaInfoList) {
            toolList.append(actionMetaInfo.actionName())
                    .append(": ")
                    .append(actionMetaInfo.desc()).append("\n");
            agentNames.append(actionMetaInfo.actionName()).append(", ");
        }

        // Remove trailing comma and space from agentNames
        if (!agentNames.isEmpty()) {
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
                    .append(ClassUtils.getFieldValues(action.getActionInput()))
                    .append("\nObservation:")
                    .append(action.getObservation());
        }
        // Replace [{agent_names}] with the agent names
        String finalFormatInstructions = OpenAITemplate.FORMAT_INSTRUCTIONS.replace("{agent_names}", agentNames.toString());

        // Combine PREFIX, dynamically generated TOOL_LIST, FORMAT_INSTRUCTIONS, and SUFFIX
        return OpenAITemplate.PREFIX + toolList + finalFormatInstructions + actionBody;
    }

}
