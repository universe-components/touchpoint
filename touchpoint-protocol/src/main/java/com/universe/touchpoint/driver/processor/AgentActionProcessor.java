package com.universe.touchpoint.driver.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultDispatcher;
import com.universe.touchpoint.driver.ResultProcessor;
import com.universe.touchpoint.router.RouteTable;

import java.util.List;
import java.util.Map;

public class AgentActionProcessor<ActionInput extends TouchPoint, T extends TouchPoint> extends ResultProcessor<AgentAction<ActionInput>, T> {

    public AgentActionProcessor(AgentAction<ActionInput> result,
                                String goal, String task, Context context, Transport transportType) {
        super(result, goal, task, context, transportType);
    }

    @Override
    public String process() {
        AIModelConfig modelConfig = ConfigManager.selectModel(goal, result.getMeta(), task);

        List<AgentActionMetaInfo> nextActions = RouteTable.getInstance().getSuccessors(result.getAction());

        String input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(nextActions, result, goal);

        Map<Object, List<Object>> choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<Object, Object> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        Pair<List<AgentAction<ActionInput>>, AgentFinish> answer = choiceParser.parse(choices);

        for (AgentAction<ActionInput> agentAction : answer.first) {
            ResultDispatcher.run(agentAction, agentAction.getMeta(), context);
        }
        return null;
    }

}
