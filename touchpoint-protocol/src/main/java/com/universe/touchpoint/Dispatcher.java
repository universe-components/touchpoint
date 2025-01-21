package com.universe.touchpoint;

import android.util.Pair;

import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.ai.ChoiceParser;
import com.universe.touchpoint.ai.ChoiceParserFactory;
import com.universe.touchpoint.ai.prompt.PromptBuilder;
import com.universe.touchpoint.ai.AIModelSelector;
import com.universe.touchpoint.arp.AgentRouteItem;
import com.universe.touchpoint.arp.AgentRouter;

public class Dispatcher {

    public static String dispatch(String content) {
        return loopCall(null, content, null);
    }

    public static <C> String loopCall(AIModelResponse.AgentAction action, String content, String routeChunk) {
        AIModelType modelType = AIModelSelector.selectModel(content);
        if (modelType == null) {
            throw new RuntimeException("unknown model type");
        }

        String input = PromptBuilder.createPromptGenerator(modelType).generatePrompt(
                AgentRouter.routeItems(Agent.getProperty("name")), action, content);

        // 推理并获取choice，随机选择一个choice
        C choice = AIModelFactory.callModel(input, modelType);
        ChoiceParser<C> choiceParser = ChoiceParserFactory.selectParser(modelType);
        Pair<AIModelResponse.AgentAction, AIModelResponse.AgentFinish> answer = choiceParser.parse(choice);

        TouchPoint touchPoint = null;
        if (answer.second != null) {
            String[] routerItem = AgentRouter.splitChunk(routeChunk);
            touchPoint = TouchPointContextManager.generateTouchPoint(
                    answer.second,
                    AgentRouter.buildChunk(routerItem[1], routerItem[0]),
                    content);
        }
        if (answer.first != null) {
            AgentRouteItem routeItem = AgentRouter.routeTo(answer.first);
            assert routeItem != null;
            touchPoint = TouchPointContextManager.generateTouchPoint(
                    answer.first,
                    AgentRouter.buildChunk(routeItem),
                    content);
        }

        assert touchPoint != null;
        boolean rs = touchPoint.finish();
        if (!rs) {
            throw new RuntimeException("send target agent failed");
        }

        loopCall(answer.first, content, null);
        throw new RuntimeException("unknown error");
    }

}
