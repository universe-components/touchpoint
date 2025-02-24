package com.universe.touchpoint.plan.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.context.TouchPoint;
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
import java.util.Map;

public class AgentActionProcessor<I extends TouchPoint, O extends TouchPoint> implements ResultProcessor<AgentAction<I, O>> {

    @Override
    public <NewInput extends TouchPoint, NewOutput extends TouchPoint> Pair<List<AgentAction<NewInput, NewOutput>>, AgentFinish> process(AgentAction<I, O> result, String goal, String task, Context context, Transport transportType) {
        LangModelConfig modelConfig = ConfigManager.selectModel(goal, result.getMeta(), task);

        List<AgentActionMetaInfo> nextActions = Router.route(result, true);;

        String input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(nextActions, result, goal);
        Map<Object, List<Object>> choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<Object, Object> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        return choiceParser.parse(choices, result);
    }

}
