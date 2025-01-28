package com.universe.touchpoint;

import android.util.Pair;

import com.universe.touchpoint.agent.Agent;
import com.universe.touchpoint.agent.AgentAction;
import com.universe.touchpoint.agent.AgentFinish;
import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.ai.AIModelSelector;
import com.universe.touchpoint.config.AIModelConfig;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.router.AgentRouter;

import java.util.List;
import java.util.Map;

public class Dispatcher {

    public static String dispatch(String content) {
        return loopCall(null, content, null);
    }

    public static <C, R> String loopCall(AgentAction action, String content, String routeChunk) {
        AIModelConfig modelConfig = AIModelSelector.selectModel(content, action);

        String input = PromptBuilder.createPromptGenerator(modelConfig.getType()).generatePrompt(
                AgentRouter.routeItems(Agent.getName()), action, content);

        Map<C, List<R>> choices = AIModelFactory.callModel(input, modelConfig);
        ChoiceParser<C, R> choiceParser = ChoiceParserFactory.selectParser(modelConfig.getType());
        Pair<List<AgentAction>, AgentFinish> answer = choiceParser.parse(choices);

        TouchPoint touchPoint = null;
        AgentAction nextAction = null;
        if (answer.second != null) {
            String[] routerItem = AgentRouter.splitChunk(routeChunk);
            touchPoint = TouchPointContextManager.generateTouchPoint(
                    answer.second,
                    AgentRouter.buildChunk(routerItem[1], routerItem[0]),
                    content);
            if (action.getMeta().transportConfig().transportType() != null) {
                return touchPoint.toString();
            }
        }
        if (answer.first != null) {
            nextAction = answer.first.get(answer.first.size() - 1);
            AgentRouteEntry routeItem = AgentRouter.routeTo(nextAction);
            assert routeItem != null;
            touchPoint = TouchPointContextManager.generateTouchPoint(
                    nextAction,
                    AgentRouter.buildChunk(routeItem),
                    content);
        }

        assert touchPoint != null;
        boolean rs = touchPoint.finish();
        if (!rs) {
            throw new RuntimeException("send target agent failed");
        }

        loopCall(nextAction, content, null);
        throw new RuntimeException("unknown error");
    }

}
