package com.universe.touchpoint.ai.prompt.generators;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.ai.prompt.PromptGenerator;
import com.universe.touchpoint.ai.prompt.template.OpenAITemplate;
import com.universe.touchpoint.api.executor.ImageActionExecutor;
import com.universe.touchpoint.context.TouchPoint;
import com.universe.touchpoint.utils.ClassUtils;

import java.util.Arrays;
import java.util.List;

public class OpenAIPromptGenerator implements PromptGenerator {

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
            boolean isImageAction;
            String actionName;
            String actionInput;
            String observation;
            try {
                isImageAction = ClassUtils.implementsInterface(Class.forName(action.getMeta().getClassName()), ImageActionExecutor.class);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            if (isImageAction) {
                finalSuffix = finalSuffix.replace("{agent_scratchpad}", "I have visual and text features to help answer the question.");
                actionName = "Use visual and text features to generate an answer.";
                actionInput = String.join(", ", Arrays.stream((Double[]) action.getOutput()).map(String::valueOf).toArray(String[]::new));
                observation = "Waiting for the model's response.";
            } else {
                finalSuffix = finalSuffix.replace("{agent_scratchpad}", action.getThought());
                actionName = action.getActionName();
                actionInput = ClassUtils.getFieldValues(action.getInput());
                observation = action.getOutput().toString();
            }
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
