package com.universe.touchpoint;

import android.util.Pair;

import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.AIModelManager;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.ai.AIModelSelector;
import com.universe.touchpoint.router.AgentRouteEntry;
import com.universe.touchpoint.router.AgentRouter;

import java.util.List;
import java.util.Map;

public class Dispatcher {

    public static String dispatch(String content) {
        return loopCall(null, content, null);
    }

    public static <C, R> String loopCall(AIModelResponse.AgentAction action, String content, String routeChunk) {
        AIModelType modelType = AIModelSelector.selectModel(content);
        if (modelType == null) {
            throw new RuntimeException("unknown model type");
        }

        String input = PromptBuilder.createPromptGenerator(modelType).generatePrompt(
                AgentRouter.routeItems(Agent.getProperty("name")), action, content);

        Map<C, List<R>> choices = AIModelFactory.callModel(input, modelType);
        ChoiceParser<C, R> choiceParser = ChoiceParserFactory.selectParser(modelType);
        Pair<List<AIModelResponse.AgentAction>, AIModelResponse.AgentFinish> answer = choiceParser.parse(choices);

        TouchPoint touchPoint = null;
        AIModelResponse.AgentAction nextAction = null;
        if (answer.second != null) {
            String[] routerItem = AgentRouter.splitChunk(routeChunk);
            touchPoint = TouchPointContextManager.generateTouchPoint(
                    answer.second,
                    AgentRouter.buildChunk(routerItem[1], routerItem[0]),
                    content);
        }
        if (answer.first != null) {
            nextAction = answer.first.get(answer.first.size() - 1);
            AgentRouteEntry routeItem = AgentRouter.routeTo(nextAction);
            assert routeItem != null;
            nextAction.setActionInputValue(
                    AIModelManager.getInstance().paddingActionInput(nextAction, Agent.getProperty("name")));
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
