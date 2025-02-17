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
import com.universe.touchpoint.config.ai.AIModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.driver.ResultDispatcher;
import com.universe.touchpoint.driver.ResultProcessor;
import com.universe.touchpoint.router.RouteTable;

import java.util.List;
import java.util.Map;

public class AgentActionProcessor<I extends TouchPoint, O extends TouchPoint> implements ResultProcessor<AgentAction<I, O>> {

    @Override
    public String process(AgentAction<I, O> result,
                          String goal, String task, Context context, Transport transportType) {
        AIModelConfig modelConfig = ConfigManager.selectModel(goal, result.getMeta(), task);

        List<AgentActionMetaInfo> nextActions = RouteTable.getInstance().getSuccessors(result.getAction());

        String input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(nextActions, result, goal);

        Map<Object, List<Object>> choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<Object, Object> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        Pair<List<AgentAction<I, O>>, AgentFinish> answer = choiceParser.parse(choices, result);

        for (AgentAction<I, O> agentAction : answer.first) {
            ResultDispatcher.run(agentAction, agentAction.getMeta(), context);
        }
        return null;
    }

}
