package com.universe.touchpoint.ai.prompt.generators;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.ai.prompt.template.OpenAITemplate;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.utils.ClassUtils;
import java.util.List;

public class OpenAIPromptGenerator implements PromptGenerator<String> {

    @RequiresApi(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    @Override
    public <I extends TouchPoint, O> String generatePrompt(@NonNull List<AgentActionMetaInfo> nextActions, AgentAction<I, O> action, String question) {
        StringBuilder toolList = new StringBuilder();
        StringBuilder agentNames = new StringBuilder();

        for (AgentActionMetaInfo actionMetaInfo : nextActions) {
            toolList.append(actionMetaInfo.getActionName())
                    .append(": ")
                    .append(actionMetaInfo.getDesc()).append("\n");
            agentNames.append(actionMetaInfo.getActionName()).append(", ");
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
