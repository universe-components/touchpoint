package com.universe.touchpoint;

import android.util.Pair;

import com.universe.touchpoint.ai.AIModelFactory;
import com.universe.touchpoint.ai.AIModelResponse;
import com.universe.touchpoint.ai.AIModelType;
import com.universe.touchpoint.dispatcher.ChoiceParser;
import com.universe.touchpoint.dispatcher.ChoiceParserFactory;
import com.universe.touchpoint.dispatcher.prompt.PromptBuilder;
import com.universe.touchpoint.dispatcher.routers.AIModelRouter;
import com.universe.touchpoint.dispatcher.AgentRouteItem;
import com.universe.touchpoint.dispatcher.routers.AgentRouter;

public class Dispatcher {

    public static String dispatch(String content) {
        AIModelType modelType = new AIModelRouter().routeTo(content);
        if (modelType == null) {
            throw new RuntimeException("unknown model type");
        }
        return loopCall(null, content, modelType);
    }

    public static <C> String loopCall(AIModelResponse.AgentAction action, String content, AIModelType modelType) {
        AgentRouter agentRouter = new AgentRouter();
        String input = PromptBuilder.createPromptGenerator(modelType).generatePrompt(
                agentRouter.agentRouteItems(Agent.getProperty("name")), action, content);

        // 推理并获取choice，随机选择一个choice
        C choice = AIModelFactory.callModel(input, modelType);
        ChoiceParser<C> choiceParser = ChoiceParserFactory.selectParser(modelType);
        Pair<AIModelResponse.AgentAction, AIModelResponse.AgentFinish> answer = choiceParser.parse(choice);
        if (answer.second != null) {
            return answer.second.getOutput();
        }

        AgentRouteItem routeItem = new AgentRouter().routeTo(answer.first);
        AIModelResponse.AgentAction touchPoint = TouchPointContextManager.generateTouchPoint(
                AIModelResponse.AgentAction.class, routeItem.getToAgent().getName());
        boolean rs = touchPoint.finish();
        if (!rs) {
            throw new RuntimeException("send target agent failed");
        }

        loopCall(answer.first, content, modelType);

        return null;
    }

}
