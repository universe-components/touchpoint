package com.universe.touchpoint.plan.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.config.ai.LangModelConfig;
import com.universe.touchpoint.config.ConfigManager;
import com.universe.touchpoint.config.transport.Transport;
import com.universe.touchpoint.plan.ResultProcessor;
import com.universe.touchpoint.router.Router;
import java.util.List;

public class AgentActionProcessor implements ResultProcessor<AgentAction<?, ?>> {

    @Override
    public <CH> Pair<List<AgentAction<?, ?>>, AgentFinish<?>> process(AgentAction<?, ?> result, String goal, String task, Context context, Transport transportType) {
        LangModelConfig modelConfig = ConfigManager.selectModel(goal, result.getMeta(), task);

        List<AgentActionMetaInfo> nextActions = Router.route(result, true);

        Object input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(nextActions, result, goal);
        Object choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<CH> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        return choiceParser.parse((CH) choices, result);
    }

}
