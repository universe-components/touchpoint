package com.universe.touchpoint.driver.processor;

import android.content.Context;
import android.util.Pair;

import com.universe.touchpoint.TouchPoint;
import com.universe.touchpoint.TouchPointContextManager;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentActionMetaInfo;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.AIModelSelector;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.api.TouchPointListener;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.config.Transport;
import com.universe.touchpoint.driver.ResultProcessor;
import com.universe.touchpoint.router.RouteTable;

import java.util.List;
import java.util.Map;

public class AgentActionProcessor<T extends TouchPoint> extends ResultProcessor<AgentAction, T> {

    public AgentActionProcessor(AgentAction result,
                                String goal, String task, TouchPointListener<T, ?> tpReceiver, Context context, Transport transportType) {
        super(result, goal, task, tpReceiver, context, transportType);
    }

    @Override
    public String process() {
        if (tpReceiver != null) {
            String actionResult = tpReceiver.onReceive(
                    (T) result.getActionInput(), context).toString();
            result.setObservation(actionResult);
        }

        AIModelConfig modelConfig = AIModelSelector.selectModel(goal, result);

        List<AgentActionMetaInfo> nextActions = RouteTable.getInstance().getSuccessors(result == null ? task : result.getAction());

        String input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(nextActions, result, goal);

        Map<Object, List<Object>> choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<Object, Object> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        Pair<List<AgentAction>, AgentFinish> answer = choiceParser.parse(choices);

        TouchPointContextManager.generateTouchPoint(answer.second, goal).finish();
        return null;
    }

}
