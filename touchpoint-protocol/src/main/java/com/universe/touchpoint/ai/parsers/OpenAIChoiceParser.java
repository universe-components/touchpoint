package com.universe.touchpoint.ai.parsers;

import android.util.Pair;
import com.openai.models.ChatCompletion;
import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionManager;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.ChoiceParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAIChoiceParser implements ChoiceParser<Map<ChatCompletion, List<ChatCompletion.Choice>>> {

    private static final String FINAL_ANSWER_ACTION = "Final Answer: ";

    @Override
    public Pair<List<AgentAction<?, ?>>, AgentFinish<?>> parse(Map<ChatCompletion, List<ChatCompletion.Choice>> choices, AgentAction<?, ?> currentAction) {
        List<AgentAction<?, ?>> agentActions = new ArrayList<>();

        for (Map.Entry<ChatCompletion, List<ChatCompletion.Choice>> entry : choices.entrySet()) {
            List<ChatCompletion.Choice> choiceList = entry.getValue(); // 对应的 Choice 列表
            for (ChatCompletion.Choice choice : choiceList) {
                String text = choice.message().content().get();
                if (text.contains(FINAL_ANSWER_ACTION)) {
                    // 如果文本中包含 FINAL_ANSWER_ACTION，返回 AgentFinish
                    return new Pair<>(null, new AgentFinish<>(text.split(FINAL_ANSWER_ACTION)[1].trim(), currentAction.getMeta()));
                }

                // 正则表达式，用于匹配 Action 和 Action Input
                String regex = "Thought\\s*:\\s*(.*?)\\nAction\\s*\\d*\\s*:(.*?)\\nAction\\s*\\d*\\s*Input\\s*\\d*\\s*:[\\s]*(.*)";
                Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(text);

                // 如果没有匹配到，抛出异常
                if (!matcher.find()) {
                    throw new RuntimeException("Could not parse LLM output: `" + text + "`");
                }

                // 提取 Action 和 Action Input、Thought
                String action = Objects.requireNonNull(matcher.group(2)).trim();
                String actionInput = matcher.group(3) == null ? "" : Objects.requireNonNull(matcher.group(2)).trim();
                String thought = matcher.group(1) == null ? "" : Objects.requireNonNull(matcher.group(1)).trim();

                currentAction.setActionName(action);
                currentAction.setThought(thought);
                currentAction.setInput(AgentActionManager
                        .getInstance()
                        .paddingActionInput(
                                action, actionInput.replaceAll("\"", "").trim(), Agent.getName()
                        ));
                agentActions.add(currentAction);
            }
        }
        // 返回 AgentAction
        return new Pair<>(agentActions, null);
    }

}
